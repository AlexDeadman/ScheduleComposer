package com.alexdeadman.auddistandroid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexdeadman.auddistandroid.data.model.DataList
import com.alexdeadman.auddistandroid.data.model.entity.Attributes
import com.alexdeadman.auddistandroid.data.model.entity.Entity
import com.alexdeadman.auddistandroid.data.model.entity.Relationships
import com.alexdeadman.auddistandroid.utils.state.ListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class AbstractViewModel(
    private val get: suspend () -> DataList<out Entity<out Attributes, out Relationships>>
) : ViewModel() {

    private val _state = MutableLiveData<ListState>()
    val state: LiveData<ListState> = _state

    init {
        fetchEntities()
    }

    fun fetchEntities() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = get()
                _state.postValue(
                    if (result.data.isEmpty()) ListState.NoItems
                    else ListState.Loaded(result)
                )
            } catch (e: Exception) {
                _state.postValue(
                    ListState.Error(e.message.toString())
                )
            }
        }
    }
}