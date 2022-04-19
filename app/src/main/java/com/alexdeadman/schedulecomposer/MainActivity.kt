package com.alexdeadman.schedulecomposer

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.util.Keys
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        fun updateTheme() {
            val theme: String? = preferences.getString(Keys.THEME, Keys.SYSTEM)

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