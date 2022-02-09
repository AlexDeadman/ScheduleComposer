package com.example.auddistandroid.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.data.AudDistRepository
import com.example.auddistandroid.utils.state.ListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val repository: AudDistRepository
) : ViewModel() {

    private val _state = MutableLiveData<ListState>()
    val state: LiveData<ListState> get() = _state

    fun fetchEntities() {
        _state.value = ListState.Loading

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = preferences.getString("authToken", null)!!
                val dataList = repository.getLecturers(token) // TODO TEMPO

                _state.postValue(
                    if (dataList.data.isEmpty()) ListState.NoItems
                    else ListState.Loaded(dataList)
                )
            } catch (e: Exception) {
                _state.postValue(
                    ListState.Error(e.message.toString())
                )
            }

        }
    }
}