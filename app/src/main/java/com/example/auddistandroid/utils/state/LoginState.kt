package com.example.auddistandroid.utils.state

sealed class LoginState {
    object Sending : LoginState()
    object Success : LoginState()
    class Error(val message: String) : LoginState()
}
