package com.alexdeadman.auddistandroid.utils.state

import com.alexdeadman.auddistandroid.data.model.DataList
import com.alexdeadman.auddistandroid.data.model.entity.Attributes
import com.alexdeadman.auddistandroid.data.model.entity.Entity
import com.alexdeadman.auddistandroid.data.model.entity.Relationships

sealed class ListState {
    object NoItems : ListState()
    class Loaded(val result: DataList<out Entity<out Attributes, out Relationships>>) : ListState()
    class Error(val message: String) : ListState()
}