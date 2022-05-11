package com.alexdeadman.schedulecomposer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.adapter.ListItem
import com.alexdeadman.schedulecomposer.model.Data
import com.alexdeadman.schedulecomposer.model.DataList
import com.alexdeadman.schedulecomposer.model.entity.Attributes
import com.alexdeadman.schedulecomposer.model.entity.Entity
import com.alexdeadman.schedulecomposer.util.state.ListState
import com.alexdeadman.schedulecomposer.util.state.SendingState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

abstract class AbstractEntityViewModel(
    private val gson: Gson,
    private val get: suspend () -> DataList<out Entity<out Attributes>>,
    private val post: suspend (RequestBody) -> Unit,
    private val put: suspend (Int, RequestBody) -> Unit,
    private val delete: suspend (Int) -> Unit,
) : ViewModel() {

    val listStateFlow = MutableStateFlow<ListState?>(null)
    val sendingStateFlow = MutableStateFlow<SendingState>(SendingState.Default())

    var comparator: Comparator<ListItem>? = null
    var searchQuery: String? = null

    var currentEntity: Entity<out Attributes>? = null

    init {
        getEntities()
    }

    fun getEntities() {
        viewModelScope.launch(Dispatchers.IO) {
            listStateFlow.value = try {
                val result = get()
                sendingStateFlow.value = SendingState.Default()
                if (result.data.isEmpty()) ListState.NoItems()
                else ListState.Loaded(result)
            } catch (e: Exception) {
                ListState.Error(R.string.unknown_error)
            }
        }
    }

    private fun send(method: suspend () -> Unit) {
        sendingStateFlow.value = SendingState.Sending()
        viewModelScope.launch(Dispatchers.IO) {
            sendingStateFlow.value = try {
                method()
                SendingState.Success()
            } catch (e: Exception) {
                SendingState.Error(R.string.unknown_error)
            } finally {
                SendingState.Default()
            }
        }
    }

    fun postEntity(entity: Entity<out Attributes>) {
        send { post(gson.toJson(Data(entity)).toRequestBody()) }
    }

    fun putEntity(entity: Entity<out Attributes>) {
        send { put(entity.id, gson.toJson(Data(entity)).toRequestBody()) }
    }

    fun deleteEntity(id: Int) {
        send { delete(id) }
    }
}