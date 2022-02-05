package com.example.auddistandroid.utils

sealed class State {
    object LoadingState : State()
    class LoadedState<T>(val data: T): State()
    object NoItemsState : State()
    class ErrorState(val message: String): State()
}