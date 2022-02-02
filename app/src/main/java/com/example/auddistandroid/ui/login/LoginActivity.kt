package com.example.auddistandroid.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.R
import com.example.auddistandroid.databinding.ActivityLoginBinding
import com.example.auddistandroid.ui.MainActivity
import com.example.auddistandroid.ui.ipsettings.IpSettingActivity
import com.example.auddistandroid.utils.ResponseStatus
import com.example.auddistandroid.utils.UiUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: LoginViewModel by viewModels()

        UiUtils.updateTheme()

        binding.apply {

            if (preferences.getString("authToken", null) == null) {
                container.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            } else {
                goMain()
            }

            buttonLogIn.setOnClickListener {
                progressBarLogIn.visibility = View.VISIBLE

                viewModel.username = editTextUsername.text.toString()
                viewModel.password = editTextPassword.text.toString()

                viewModel.token.observe(this@LoginActivity) {
                    if (viewModel.responseStatus != ResponseStatus.SUCCESS) {
                        textViewLogInError.text = when (viewModel.responseStatus) {
                            ResponseStatus.NO_RESPONSE -> getString(R.string.server_is_not_responding)
                            ResponseStatus.UNAUTHORIZED -> getString(R.string.unauthorized)
                            else -> getString(R.string.unknown_error)
                        }
                        textViewLogInError.visibility = View.VISIBLE
                    } else {
                        preferences.edit().apply {
                            putString("authToken", "Token $it")
                            putString("username", viewModel.username)
                            apply()
                        }

                        goMain()
                    }
                    progressBarLogIn.visibility = View.INVISIBLE
                }
            }

            buttonIpSettings.setOnClickListener {
                startActivity(Intent(this@LoginActivity, IpSettingActivity::class.java))
            }
        }
    }

    private fun goMain() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
}