package com.tullahnazari.shmack.Services

import android.graphics.Color
import com.tullahnazari.shmack.Controller.App
import java.util.*

object UserDataService {

    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun logout() {
        id = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        name = ""
        App.prefs.authToken = ""
        App.prefs.userEmail = ""
        App.prefs.isLoggedIn = false
        MessageService.clearMessages()
        MessageService.clearChannels()

    }

    fun returnAvatarColor(components: String) : Int {
        // have to break this down so it is without commmas and brackets
        // [0.2, 0.9921568627450981, 0.4745098039215686, 1]
        // 0.2 0.9921568627450981 0.4745098039215686 1

        val strippedColor = components.replace(
            "[", "")
            .replace("]", "")
            .replace(",", "")

        var r = 0
        var g = 0
        var b = 0

        val scanner = Scanner(strippedColor)
        if (scanner.hasNext()) {
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }


        return Color.rgb(r, g, b)



    }
}