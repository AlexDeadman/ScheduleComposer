package com.example.auddistandroid.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.R
import com.example.auddistandroid.ui.MainActivity
import com.example.auddistandroid.ui.dialog.LogoutConfirm
import com.example.auddistandroid.utils.Keys


class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        preferences.registerOnSharedPreferenceChangeListener(this)

        val dialog = LogoutConfirm()

        preferenceManager.findPreference<Preference>("logout")?.setOnPreferenceClickListener {
            dialog.show(parentFragmentManager, "")
            return@setOnPreferenceClickListener true
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == Keys.THEME) {
            MainActivity.updateTheme()
        }
    }
}