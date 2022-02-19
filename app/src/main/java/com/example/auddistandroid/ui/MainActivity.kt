package com.example.auddistandroid.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.R
import com.example.auddistandroid.utils.Keys
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        fun updateTheme() {
            val theme: String? = preferences.getString(Keys.THEME, Keys.LIGHT)

            AppCompatDelegate.setDefaultNightMode(
                when (theme) {
                    Keys.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    Keys.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
        }
    }
}