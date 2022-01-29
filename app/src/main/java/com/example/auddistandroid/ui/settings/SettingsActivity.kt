package com.example.auddistandroid.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.auddistandroid.App
import com.example.auddistandroid.R
import com.example.auddistandroid.utils.UiUtils


class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_frame, SettingsFragment())
                .commit()
        }
        supportActionBar?.apply {
            setTitle(R.string.settings)
            setDisplayHomeAsUpEnabled(true)
        }

        App.preferences.registerOnSharedPreferenceChangeListener(this)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val editor = App.preferences.edit()

            preferenceManager.findPreference<Preference>("logout")?.setOnPreferenceClickListener {
                // TODO logout

                editor.remove("authToken")
                editor.remove("username")
                editor.apply()

                activity?.finish()
                return@setOnPreferenceClickListener true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "theme") {
            UiUtils.updateTheme()
        }
    }
}