package com.example.shmack.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.shmack.R
import com.example.shmack.Services.AuthService
import com.example.shmack.Services.UserDataService
import com.example.shmack.Utilities.BROADCAST_USER_DATA_CHANGE
import com.example.shmack.Utilities.SOCKET_URL
import io.socket.client.IO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()



    }
    //socket lifecycle
    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE)
        )
        socket.connect()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onDestroy()
    }

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AuthService.isLoggedIn) {
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable",
                     packageName)
                userImageNavHeader.setImageResource(resourceId)
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                loginButtonNavHeader.text = "Logout"
            }
        }

    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    //creating intent to go to loginpage after clicking login button on main page
    fun loginBtnNavClicked(view: View) {

        if (AuthService.isLoggedIn) {
            //log out

            Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show()
            UserDataService.logout()
            userNameNavHeader.text = ""
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginButtonNavHeader.text = "Login"

        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun addChannelClicked(view: View) {

        //creating a view for modal/dialog on add channel click
        if (AuthService.isLoggedIn) {
            val builder = AlertDialog.Builder(this)
            val dialogview = layoutInflater.inflate(R.layout.add_channel_dialog, null)

            builder.setView(dialogview).setPositiveButton("Add") {
                dialogInterface, i ->
                //perform logic when clicked
                val nameTextField = dialogview.findViewById<EditText>(R.id.addChannelNameTxt)
                val descTextField = dialogview.findViewById<EditText>(R.id.addChannelDesc)
                val channelName = nameTextField.text.toString()
                val channelDesc = descTextField.text.toString()

                //Create channel with the channel name and desc
                socket.emit("newChannel", channelName, channelDesc)

            }
                    //cancel and close the dialog
                .setNegativeButton("Cancel") {
                    dialogInterface, i ->
                }
                .show()
        }
    }

    fun sendMsgBtnClicked(view: View) {

    }
}
