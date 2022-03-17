package com.alexdeadman.auddistandroid

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.pluto.Pluto
import com.pluto.plugins.network.PlutoNetworkPlugin
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var preferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

//        Pluto.Installer(this)
//            .addPlugin(PlutoNetworkPlugin("network"))
//            .install()

        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }
}