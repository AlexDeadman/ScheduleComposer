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
import com.alexdeadman.schedulecomposer.utils.Keys

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener
{
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)

        preferences.registerOnSharedPreferenceChangeListener(this)

        preferenceManager.apply {
            findPreference<Preference>(Keys.LOGGED_IN_AS)?.summary =
                preferences.getString(Keys.USERNAME, "Unknown")

            findPreference<Preference>(Keys.LOGOUT)?.setOnPreferenceClickListener {
                LogoutConfirmDialog().show(parentFragmentManager, null)
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>(Keys.ABOUT)?.summary =
                resources.getString(R.string.version, BuildConfig.VERSION_NAME)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == Keys.THEME) {
            MainActivity.updateTheme()
        }
    }
}