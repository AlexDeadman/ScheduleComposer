package com.example.auddistandroid.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.auddistandroid.data.AudDistRepository
import com.example.auddistandroid.ui.list.ListItem
import com.example.auddistandroid.utils.state.ListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AbstractViewModel @Inject constructor(
    private val repository: AudDistRepository
) : ViewModel() {

    private val _state = MutableLiveData<ListState>()
    val state: LiveData<ListState> get() = _state

    init {
        fetchEntities()
    }

    private fun fetchEntities() {
        _state.value = ListState.Loading

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // TODO                     TEMPO
                val result = repository.getLecturers().data.map(::ListItem)

                _state.postValue(
                    if (result.isEmpty()) ListState.NoItems
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