package com.example.auddistandroid.ui.ipsettings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.auddistandroid.App
import com.example.auddistandroid.databinding.ActivityIpSettingsBinding

class IpSettingActivity: AppCompatActivity() {
    private lateinit var binding: ActivityIpSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIpSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}