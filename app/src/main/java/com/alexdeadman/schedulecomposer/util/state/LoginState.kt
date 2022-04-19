package com.alexdeadman.schedulecomposer.util.state

sealed class LoginState {
    object Sending : LoginState()
    object Success : LoginState()
    class Error(val messageStringId: Int) : LoginState()
}
