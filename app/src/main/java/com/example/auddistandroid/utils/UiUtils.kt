package com.example.auddistandroid.utils

import androidx.appcompat.app.AppCompatDelegate
import com.example.auddistandroid.App

class UiUtils {
    companion object {
        fun updateTheme() {
            val theme: String? = App.preferences.getString("theme", "sys_theme")
            AppCompatDelegate.setDefaultNightMode(
                when (theme) {
                    "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                    "light" -> AppCompatDelegate.MODE_NIGHT_NO
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
        }
    }
}