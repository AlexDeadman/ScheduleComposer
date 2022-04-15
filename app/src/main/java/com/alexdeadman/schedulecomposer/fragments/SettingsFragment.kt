package com.alexdeadman.schedulecomposer.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.BuildConfig
import com.alexdeadman.schedulecomposer.MainActivity
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.dialog.ConfirmDialog
import com.alexdeadman.schedulecomposer.utils.Keys

class SettingsFragment : PreferenceFragmentCompat(),
    ConfirmDialog.ConfirmationListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)

        preferences.registerOnSharedPreferenceChangeListener(this)

        preferenceManager.apply {
            findPreference<Preference>(Keys.LOGGED_IN_AS)?.summary =
                preferences.getString(Keys.USERNAME, "Unknown")

            val confirmDialog = ConfirmDialog()

            findPreference<Preference>(Keys.LOGOUT)?.setOnPreferenceClickListener {
                confirmDialog.show(childFragmentManager, null)
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>(Keys.ABOUT)?.summary =
                getString(R.string.version, BuildConfig.VERSION_NAME)
        }
    }

    override fun confirmButtonClicked() {
        preferences.edit().apply {
            remove(Keys.AUTH_TOKEN)
            remove(Keys.USERNAME)
            apply()
        }
        requireParentFragment().requireParentFragment()
            .findNavController()
            .navigate(R.id.action_mainFragment_to_connectionFragment)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == Keys.THEME) {
            MainActivity.updateTheme()
        }
    }
}