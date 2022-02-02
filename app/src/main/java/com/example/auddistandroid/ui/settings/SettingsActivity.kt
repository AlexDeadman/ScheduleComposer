package com.example.auddistandroid.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.R
import com.example.auddistandroid.ui.BaseActivity
import com.example.auddistandroid.ui.LogoutConfirmDialogFragment
import com.example.auddistandroid.utils.UiUtils


class SettingsActivity : BaseActivity(R.string.settings),
    SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_frame, SettingsFragment())
                .commit()
        }

        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val dialog = LogoutConfirmDialogFragment()

            preferenceManager.findPreference<Preference>("logout")?.setOnPreferenceClickListener {
                dialog.show(parentFragmentManager, "")
                return@setOnPreferenceClickListener true
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "theme") {
            UiUtils.updateTheme()
        }
    }
}