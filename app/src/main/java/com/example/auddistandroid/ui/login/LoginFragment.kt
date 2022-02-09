package com.example.auddistandroid.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.databinding.FragmentLoginBinding
import com.example.auddistandroid.ui.MainActivity
import com.example.auddistandroid.ui.ipsettings.IpSettingFragment
import com.example.auddistandroid.utils.UiUtils
import com.example.auddistandroid.utils.state.LoginState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UiUtils.updateTheme()

        binding.apply {

            // TODO эта проверка должна происходить на сплеш экране
            if (preferences.getString("authToken", null) != null) {
                startMainActivity()
            }

            val viewModel: LoginViewModel by viewModels()

            viewModel.state.observe(viewLifecycleOwner) {
                when(it) {
                    is LoginState.Sending -> {
                        editTextUsername.isEnabled = false
                        editTextPassword.isEnabled = false
                        progressBar.visibility = View.VISIBLE
                        textViewLogInError.visibility = View.INVISIBLE
                    }
                    is LoginState.Success -> {
                        startMainActivity()
                    }
                    is LoginState.Error -> {
                        editTextUsername.isEnabled = true
                        editTextPassword.isEnabled = true
                        progressBar.visibility = View.INVISIBLE

                        textViewLogInError.apply {
                            text = it.message // TODO TEMPO
                            visibility = View.VISIBLE
                        }
                    }
                }
            }

            buttonLogIn.setOnClickListener {
                viewModel.fetchToken(
                    editTextUsername.text.toString(),
                    editTextPassword.text.toString()
                )
            }

            buttonIpSettings.setOnClickListener {
                startActivity(Intent(requireActivity(), IpSettingFragment::class.java))
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
//        finish()
    }
}