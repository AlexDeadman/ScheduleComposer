package com.alexdeadman.schedulecomposer.utils.state

import com.alexdeadman.schedulecomposer.data.model.DataList
import com.alexdeadman.schedulecomposer.data.model.entity.Attributes
import com.alexdeadman.schedulecomposer.data.model.entity.Entity
import com.alexdeadman.schedulecomposer.data.model.entity.Relationships

sealed class ListState {
    class Loaded(val result: DataList<out Entity<out Attributes, out Relationships>>) : ListState()
    class NoItems(val messageStringId: Int) : ListState()
}