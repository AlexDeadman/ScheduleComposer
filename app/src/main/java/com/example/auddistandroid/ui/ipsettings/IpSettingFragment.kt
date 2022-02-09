package com.example.auddistandroid.ui.ipsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.databinding.FragmentIpSettingsBinding

class IpSettingFragment : Fragment() {

    private var _binding: FragmentIpSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIpSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            editTextIp.setText(preferences.getString("ip", ""))

            buttonSave.setOnClickListener {
                preferences
                    .edit()
                    .putString("ip", editTextIp.text.toString())
                    .apply()
                requireActivity().onBackPressed() // TODO бред какой-то
            }
        }
    }
}