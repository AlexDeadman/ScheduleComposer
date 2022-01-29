package com.example.auddistandroid.ui.ipsettings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.auddistandroid.App
import com.example.auddistandroid.R
import com.example.auddistandroid.databinding.ActivityIpSettingsBinding

class IpSettingActivity: AppCompatActivity() {
    private lateinit var binding: ActivityIpSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIpSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setTitle(R.string.ip_settings)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.apply {
            editTextIp.setText(App.preferences.getString("ip", ""))

            buttonSave.setOnClickListener {
                App.preferences
                    .edit()
                    .putString("ip", editTextIp.text.toString())
                    .apply()
                finish()
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
}