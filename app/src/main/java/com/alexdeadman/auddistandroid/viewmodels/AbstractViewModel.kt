package com.alexdeadman.auddistandroid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexdeadman.auddistandroid.R
import com.alexdeadman.auddistandroid.adapters.ListItem
import com.alexdeadman.auddistandroid.data.model.DataList
import com.alexdeadman.auddistandroid.data.model.entity.Entity
import com.alexdeadman.auddistandroid.fragments.MainFragment
import com.alexdeadman.auddistandroid.service.AudDistApi
import com.alexdeadman.auddistandroid.utils.state.ListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.KSuspendFunction0

@HiltViewModel
class AbstractViewModel @Inject constructor(
    audDistApi: AudDistApi
) : ViewModel() {

    private val fetchMethod: KSuspendFunction0<DataList<out Entity>> =
        audDistApi.run {
            when (MainFragment.currentDestinationId) {
                R.id.schedule -> ::getSchedule
                R.id.directions -> ::getDirections
                R.id.syllabuses -> ::getSyllabuses
                R.id.disciplines -> ::getDisciplines
                R.id.lecturers -> ::getLecturers
                R.id.groups -> ::getGroups
                R.id.audiences -> ::getAudiences
                else -> throw IllegalStateException()
            }
        }

    private val _state = MutableLiveData<ListState>()
    val state: LiveData<ListState> get() = _state

    init {
        fetchEntities()
    }

    private fun fetchEntities() {
        _state.value = ListState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = fetchMethod().data.map { item -> ListItem(item) }
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