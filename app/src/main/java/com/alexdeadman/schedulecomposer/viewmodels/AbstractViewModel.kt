package com.alexdeadman.schedulecomposer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.adapters.ListItem
import com.alexdeadman.schedulecomposer.model.Data
import com.alexdeadman.schedulecomposer.model.DataList
import com.alexdeadman.schedulecomposer.model.entity.Attributes
import com.alexdeadman.schedulecomposer.model.entity.Entity
import com.alexdeadman.schedulecomposer.utils.state.ListState
import com.alexdeadman.schedulecomposer.utils.state.ListState.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

abstract class AbstractViewModel(
    private val get: suspend () -> DataList<out Entity<out Attributes>>,
    private val post: suspend (RequestBody) -> Unit,
    private val put: suspend (Int, RequestBody) -> Unit,
    private val delete: suspend (Int) -> Unit,
) : ViewModel() {

    val fetchState = MutableStateFlow<ListState?>(null)

    var comparator: Comparator<ListItem>? = null
    var searchQuery: String? = null

    var currentEntity: Entity<out Attributes>? = null

    init {
        fetchEntities()
    }

    fun fetchEntities() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchState.value = try {
                val result = get()
                if (result.data.isEmpty()) NoItems()
                else Loaded(result)
            } catch (e: Exception) {
                Error(R.string.unknown_error /*TODO TEMPO*/)
            }
        }
    }

    fun postEntity(entity: Entity<out Attributes>) {
        // TODO error handling
        viewModelScope.launch(Dispatchers.IO) {
            post(Gson().toJson(Data(entity)).toRequestBody())
        }
    }

    fun updateEntity(entity: Entity<out Attributes>) {
        // TODO error handling
        viewModelScope.launch(Dispatchers.IO) {
            put(entity.id, Gson().toJson(Data(entity)).toRequestBody())
        }
    }

    fun deleteEntity(id: Int) {
        // TODO error handling
        viewModelScope.launch(Dispatchers.IO) {
            delete(id)
        }
    }
}