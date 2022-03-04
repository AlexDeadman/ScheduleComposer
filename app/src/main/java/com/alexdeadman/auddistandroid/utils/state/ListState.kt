package com.alexdeadman.auddistandroid.utils.state

import com.alexdeadman.auddistandroid.adapters.ListItem

sealed class ListState {
    object Loading : ListState()
    object NoItems : ListState()
    class Loaded(val result: List<ListItem>): ListState()
    class Error(val message: String): ListState()
}