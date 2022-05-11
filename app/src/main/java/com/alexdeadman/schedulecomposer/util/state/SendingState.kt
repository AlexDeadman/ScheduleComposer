package com.alexdeadman.schedulecomposer.util.state

@Suppress("CanSealedSubClassBeObject")
sealed class SendingState {
    class Default : SendingState()
    class Sending : SendingState()
    class Success : SendingState()
    class Error(val messageStringId: Int) : SendingState()
}
