package com.tullahnazari.shmack.Controller

import android.app.Application
import com.tullahnazari.shmack.Utilities.SharedPrefs

class App : Application() {

    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}