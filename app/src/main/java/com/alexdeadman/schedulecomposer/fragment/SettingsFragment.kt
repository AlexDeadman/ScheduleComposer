package com.alexdeadman.schedulecomposer.fragment

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
import com.alexdeadman.schedulecomposer.util.key.PreferenceKeys
import com.alexdeadman.schedulecomposer.util.show

class SettingsFragment : PreferenceFragmentCompat(),
    ConfirmDialog.ConfirmationListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)

        preferences.registerOnSharedPreferenceChangeListener(this)

        preferenceManager.apply {
            findPreference<Preference>(PreferenceKeys.LOGGED_IN_AS)?.summary =
                preferences.getString(PreferenceKeys.USERNAME, "Unknown")

            val confirmDialog = ConfirmDialog()

            findPreference<Preference>(PreferenceKeys.LOGOUT)?.setOnPreferenceClickListener {
                confirmDialog.show(childFragmentManager)
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>(PreferenceKeys.ABOUT)?.summary =
                getString(R.string.version, BuildConfig.VERSION_NAME)
        }
    }

    override fun onConfirm() {
        preferences.edit().apply {
            remove(PreferenceKeys.AUTH_TOKEN)
            remove(PreferenceKeys.USERNAME)
            apply()
        }
        requireParentFragment().requireParentFragment()
            .findNavController()
            .navigate(R.id.action_mainFragment_to_connectionFragment)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == PreferenceKeys.THEME) {
            MainActivity.updateTheme()
        }
    }
}