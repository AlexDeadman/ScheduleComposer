package com.example.auddistandroid.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.auddistandroid.R
import com.example.auddistandroid.databinding.FragmentLoginBinding
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

        binding.apply {

            val viewModel: LoginViewModel by viewModels()

            viewModel.state.observe(viewLifecycleOwner) {
                when(it) {
                    is LoginState.Sending -> {
                        editTextUsername.isEnabled = false
                        editTextPassword.isEnabled = false
                        progressBar.visibility = View.VISIBLE
                        textViewError.visibility = View.INVISIBLE
                    }
                    is LoginState.Success -> {
                        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    }
                    is LoginState.Error -> {
                        editTextUsername.isEnabled = true
                        editTextPassword.isEnabled = true
                        progressBar.visibility = View.INVISIBLE

                        textViewError.apply {
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
        }
    }
}