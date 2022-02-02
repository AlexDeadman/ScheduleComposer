package com.example.auddistandroid.ui.ipsettings

import android.os.Bundle
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.R
import com.example.auddistandroid.databinding.ActivityIpSettingsBinding
import com.example.auddistandroid.ui.BaseActivity

class IpSettingActivity: BaseActivity(R.string.ip_settings) {
    private lateinit var binding: ActivityIpSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIpSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            editTextIp.setText(preferences.getString("ip", ""))

            buttonSave.setOnClickListener {
                preferences
                    .edit()
                    .putString("ip", editTextIp.text.toString())
                    .apply()
                finish()
            }
        }
    }
}