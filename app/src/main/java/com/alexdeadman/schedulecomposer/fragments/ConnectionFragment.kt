package com.alexdeadman.schedulecomposer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FragmentConnectionBinding
import com.alexdeadman.schedulecomposer.utils.isValid
import com.alexdeadman.schedulecomposer.utils.keys.PreferenceKeys
import com.alexdeadman.schedulecomposer.utils.validate

class ConnectionFragment : Fragment() {

    private var _binding: FragmentConnectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            tiLayoutUrl.validate(listOf(
                { it.isNotBlank() to resources.getString(R.string.required_field) },
                { URLUtil.isValidUrl(it) to resources.getString(R.string.wrong_format) }
            ))

            tiEditTextUrl.setText(preferences.getString(PreferenceKeys.URL, ""))

            buttonContinue.setOnClickListener {
                if (tiLayoutUrl.isValid()) {
                    preferences.edit().putString(
                        PreferenceKeys.URL,
                        tiEditTextUrl.text.toString()
                    ).apply()

                    findNavController().navigate(
                        R.id.action_connectionFragment_to_loginFragment
                    )
                }
            }
        }
    }
}