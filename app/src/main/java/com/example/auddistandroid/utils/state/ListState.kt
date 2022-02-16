package com.example.auddistandroid.utils.state

import com.example.auddistandroid.ui.list.ListItem

sealed class ListState {
    object Loading : ListState()
    object NoItems : ListState()
    class Loaded(val result: List<ListItem>): ListState()
    class Error(val message: String): ListState()
}