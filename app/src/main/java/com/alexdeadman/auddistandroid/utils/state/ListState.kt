package com.alexdeadman.auddistandroid.utils.state

import com.alexdeadman.auddistandroid.data.model.DataList
import com.alexdeadman.auddistandroid.data.model.entity.Entity

sealed class ListState {
    object NoItems : ListState()
    class Loaded(val result: DataList<out Entity>) : ListState()
    class Error(val message: String) : ListState()
}