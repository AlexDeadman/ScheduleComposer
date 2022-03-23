package com.alexdeadman.schedulecomposer.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.MainActivity
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.dialog.LogoutConfirmDialog
import com.alexdeadman.schedulecomposer.utils.PreferenceKeys


class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)

        preferences.registerOnSharedPreferenceChangeListener(this)

        val dialog = LogoutConfirmDialog()

        preferenceManager.findPreference<Preference>("logout")?.setOnPreferenceClickListener {
            dialog.show(parentFragmentManager, "")
            return@setOnPreferenceClickListener true
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == PreferenceKeys.THEME) {
            MainActivity.updateTheme()
        }
    }
}