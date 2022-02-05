package com.example.auddistandroid.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.data.AudDistRepository
import com.example.auddistandroid.extentions.default
import com.example.auddistandroid.extentions.set
import com.example.auddistandroid.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    val repository: AudDistRepository
) : ViewModel() {

    val state: MutableLiveData<State> = MutableLiveData<State>().default(State.LoadingState)

    fun fetchEntities() {

        state.set(State.LoadingState)

        CoroutineScope(Dispatchers.IO).launch {
            try {

                val token = preferences.getString("authToken", null)!!
                val dataList = repository.getLecturers(token) // TODO TEMPO replace with lambda

                launch(Dispatchers.Main) {
                    state.set(
                        if (dataList.data.isEmpty()) State.NoItemsState
                        else State.LoadedState(dataList)
                    )
                }
            } catch (e: Exception) {

                launch(Dispatchers.Main) {
                    state.set(State.ErrorState(e.message.toString()))
                }
            }

        }
    }
}