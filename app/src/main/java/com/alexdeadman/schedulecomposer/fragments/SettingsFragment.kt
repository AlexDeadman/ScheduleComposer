package com.alexdeadman.schedulecomposer.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.BuildConfig
import com.alexdeadman.schedulecomposer.MainActivity
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.dialog.LogoutConfirmDialog
import com.alexdeadman.schedulecomposer.utils.keys.PreferenceKeys

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener
{
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)

        preferences.registerOnSharedPreferenceChangeListener(this)

        preferenceManager.apply {
            findPreference<Preference>(PreferenceKeys.LOGGED_IN_AS)?.summary =
                preferences.getString(PreferenceKeys.USERNAME, "Unknown")

            val logoutConfirmDialog = LogoutConfirmDialog()
            findPreference<Preference>(PreferenceKeys.LOGOUT)?.setOnPreferenceClickListener {
                logoutConfirmDialog.show(parentFragmentManager, "")
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>(PreferenceKeys.ABOUT)?.summary =
                resources.getString(R.string.version, BuildConfig.VERSION_NAME)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == PreferenceKeys.THEME) {
            MainActivity.updateTheme()
        }
    }
}