package com.tullahnazari.shmack.Controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.tullahnazari.shmack.R
import com.tullahnazari.shmack.Services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE
    }

    fun loginLoginBtnClicked(view: View) {
        enableSpinner(true)
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyboard()

        if (email.isNotEmpty() && password.isNotEmpty()) {

            AuthService.loginUser(email, password) {
                    loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) {
                            findSuccess ->
                        if (findSuccess) {
                            enableSpinner(false)
                            finish()
                        } else {
                            errorToast()
                        }

                    }
                } else {
                    errorToast()
                }
            }

        } else {
            Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_LONG).show()
        }

    }

    fun loginCreateUserBtnClicked(view: View) {
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()

    }

    //global toast message if something goes wrong
    fun errorToast() {
        Toast.makeText(this, "oops, something went wrong. Please try again", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    //enabling spinner on load
    fun enableSpinner(enable : Boolean) {
        if (enable) {
            loginSpinner.visibility = View.VISIBLE

        } else {
            loginSpinner.visibility = View.INVISIBLE

        }
        loginLoginBtn.isEnabled = !enable
        loginLoginBtn.isEnabled = !enable
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE)
        as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }


}
