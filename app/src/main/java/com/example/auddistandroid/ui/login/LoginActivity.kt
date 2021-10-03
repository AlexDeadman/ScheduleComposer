package com.example.auddistandroid.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.auddistandroid.R
import com.example.auddistandroid.databinding.ActivityLoginBinding
import com.example.auddistandroid.ui.MainActivity
import com.example.auddistandroid.ui.QueryStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: LoginViewModel by viewModels()

        binding.apply {
            buttonLogIn.setOnClickListener {
                progressBar.visibility = View.VISIBLE

                viewModel.username = editTextUsername.text.toString()
                viewModel.password = editTextPassword.text.toString()

                viewModel.token.observe(this@LoginActivity) {
                    if (viewModel.queryStatus != QueryStatus.SUCCESS) {
                        textViewLogInError.text = when (viewModel.queryStatus) {
                            QueryStatus.NO_INTERNET -> getString(R.string.no_internet_connection)
                            QueryStatus.NO_RESPONSE -> getString(R.string.server_is_not_responding)
                            QueryStatus.UNAUTHORIZED -> getString(R.string.unauthorized)
                            else -> getString(R.string.unknown_error)
                        }
                        textViewLogInError.visibility = View.VISIBLE
                    } else {
//                        Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                        startActivity(
                            Intent(this@LoginActivity, MainActivity::class.java)
                        )
                    }
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}