package com.alexdeadman.schedulecomposer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FragmentLoginBinding
import com.alexdeadman.schedulecomposer.util.isValid
import com.alexdeadman.schedulecomposer.util.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.util.state.SendingState
import com.alexdeadman.schedulecomposer.util.validate
import com.alexdeadman.schedulecomposer.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val viewModel: LoginViewModel by viewModels()

            viewModel.state.launchRepeatingCollect(viewLifecycleOwner) { state ->
                when (state) {
                    is SendingState.Default -> {}
                    is SendingState.Sending -> {
                        tiEditTextUsername.isEnabled = false
                        tiEditTextPassword.isEnabled = false
                        buttonLogin.isEnabled = false
                        progressBar.visibility = View.VISIBLE
                        textViewError.visibility = View.INVISIBLE
                    }
                    is SendingState.Success -> {
                        findNavController().navigate(
                            R.id.action_loginFragment_to_mainFragment
                        )
                    }
                    is SendingState.Error -> {
                        tiEditTextUsername.isEnabled = true
                        tiEditTextPassword.isEnabled = true
                        buttonLogin.isEnabled = true
                        progressBar.visibility = View.INVISIBLE

                        textViewError.apply {
                            text = getString(state.messageStringId)
                            visibility = View.VISIBLE
                        }
                    }
                }
            }

            tiLayoutUsername.validate(listOf(
                { it.isNotBlank() to getString(R.string.required_field) },
                { (it.length < 150) to getString(R.string.wrong_format) }
            ))
            tiLayoutPassword.validate(listOf {
                it.isNotBlank() to getString(R.string.required_field)
            })

            buttonLogin.setOnClickListener {
                if (tiLayoutUsername.isValid() && tiLayoutPassword.isValid()) {
                    viewModel.getToken(
                        tiEditTextUsername.text.toString(),
                        tiEditTextPassword.text.toString()
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}