package com.alexdeadman.schedulecomposer

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.utils.keys.PreferenceKeys
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        fun updateTheme() {
            val theme: String? = preferences.getString(PreferenceKeys.THEME, PreferenceKeys.SYSTEM)

            AppCompatDelegate.setDefaultNightMode(
                when (theme) {
                    PreferenceKeys.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    PreferenceKeys.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
        }
    }
}