package com.alexdeadman.schedulecomposer.utils.state

import com.alexdeadman.schedulecomposer.model.DataList
import com.alexdeadman.schedulecomposer.model.entity.Attributes
import com.alexdeadman.schedulecomposer.model.entity.Entity

@Suppress("CanSealedSubClassBeObject")
sealed class ListState {
    class Loaded(val result: DataList<out Entity<out Attributes>>) : ListState()
    class NoItems: ListState()
    class Error(val messageStringId: Int) : ListState()
}