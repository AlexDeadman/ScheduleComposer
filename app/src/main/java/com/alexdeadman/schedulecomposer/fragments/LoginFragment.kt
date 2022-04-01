package com.alexdeadman.schedulecomposer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FragmentLoginBinding
import com.alexdeadman.schedulecomposer.utils.isValid
import com.alexdeadman.schedulecomposer.utils.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.utils.state.LoginState.*
import com.alexdeadman.schedulecomposer.utils.validate
import com.alexdeadman.schedulecomposer.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull

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

            viewModel.state
                .filterNotNull()
                .launchRepeatingCollect(viewLifecycleOwner) { state ->
                    when (state) {
                        is Sending -> {
                            tiEditTextUsername.isEnabled = false
                            tiEditTextPassword.isEnabled = false
                            progressBar.visibility = View.VISIBLE
                            textViewError.visibility = View.INVISIBLE
                        }
                        is Success -> {
                            findNavController().navigate(
                                R.id.action_loginFragment_to_mainFragment
                            )
                        }
                        is Error -> {
                            tiEditTextUsername.isEnabled = true
                            tiEditTextPassword.isEnabled = true
                            progressBar.visibility = View.INVISIBLE

                            textViewError.apply {
                                text = resources.getString(state.messageStringId)
                                visibility = View.VISIBLE
                            }
                        }
                    }
                }

            tiLayoutUsername.validate(listOf(
                { it.isNotBlank() to resources.getString(R.string.required_field) },
                { (it.length < 150) to resources.getString(R.string.wrong_format) }
            ))
            tiLayoutPassword.validate(listOf {
                it.isNotBlank() to resources.getString(R.string.required_field)
            })

            buttonLogin.setOnClickListener {
                if (tiLayoutUsername.isValid() && tiLayoutPassword.isValid()) {
                    viewModel.fetchToken(
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