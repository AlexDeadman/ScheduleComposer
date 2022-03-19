package com.alexdeadman.schedulecomposer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FragmentConnectionBinding
import com.alexdeadman.schedulecomposer.utils.Keys

class ConnectionFragment : Fragment() {

    private var _binding: FragmentConnectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            editTextUrl.setText(preferences.getString(Keys.URL, ""))

            buttonContinue.setOnClickListener {

                if (editTextUrl.text!!.isNotBlank()) { // TODO validation

                    val url = editTextUrl.text.toString()

                    preferences
                        .edit()
                        .putString(Keys.URL, url)
                        .apply()

                    findNavController().navigate(R.id.action_connectionFragment_to_loginFragment)
                } else {
                    textViewError.apply {
                        text = getString(R.string.incorrect_url)
                        visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}