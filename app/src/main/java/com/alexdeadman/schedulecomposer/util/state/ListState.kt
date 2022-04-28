package com.alexdeadman.schedulecomposer.util.state

import com.alexdeadman.schedulecomposer.model.DataList
import com.alexdeadman.schedulecomposer.model.entity.Attributes
import com.alexdeadman.schedulecomposer.model.entity.Entity

sealed class ListState {
    class Loaded(val result: DataList<out Entity<out Attributes>>) : ListState()
    object NoItems : ListState()
    class Error(val messageStringId: Int) : ListState()
}