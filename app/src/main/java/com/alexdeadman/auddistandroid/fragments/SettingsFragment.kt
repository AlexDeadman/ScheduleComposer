package com.alexdeadman.auddistandroid.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.alexdeadman.auddistandroid.App.Companion.preferences
import com.alexdeadman.auddistandroid.MainActivity
import com.alexdeadman.auddistandroid.R
import com.alexdeadman.auddistandroid.dialog.LogoutConfirm
import com.alexdeadman.auddistandroid.utils.Keys


class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)

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