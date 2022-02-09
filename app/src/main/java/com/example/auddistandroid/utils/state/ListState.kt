package com.example.auddistandroid.utils.state

sealed class ListState {
    object Loading : ListState()
    object NoItems : ListState()
    class Loaded<T>(val data: T): ListState()
    class Error(val message: String): ListState()
}