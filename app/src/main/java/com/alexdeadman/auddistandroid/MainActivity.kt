package com.alexdeadman.auddistandroid

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.alexdeadman.auddistandroid.App.Companion.preferences
import com.alexdeadman.auddistandroid.utils.Keys
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