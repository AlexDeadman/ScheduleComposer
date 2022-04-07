package com.alexdeadman.schedulecomposer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.adapters.ListItem
import com.alexdeadman.schedulecomposer.model.DataList
import com.alexdeadman.schedulecomposer.model.entity.Attributes
import com.alexdeadman.schedulecomposer.model.entity.Entity
import com.alexdeadman.schedulecomposer.utils.state.ListState
import com.alexdeadman.schedulecomposer.utils.state.ListState.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class AbstractViewModel(
    private val get: suspend () -> DataList<out Entity<out Attributes>>
) : ViewModel() {

    val state = MutableStateFlow<ListState?>(null)
    var comparator: Comparator<ListItem>? = null
    var searchQuery: String? = null

    init {
        fetchEntities()
    }

    fun fetchEntities() {
        viewModelScope.launch(Dispatchers.IO) {
            state.value = try {
                val result = get()
                if (result.data.isEmpty()) NoItems
                Loaded(result)
            } catch (e: Exception) {
                Error(R.string.unknown_error /*TODO TEMPO*/)
            }
        }
    }
}