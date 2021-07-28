package com.example.auddistandroid.ui.lecturers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.auddistandroid.data.AudDistRepository
import com.example.auddistandroid.data.model.LecturersList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class LecturersViewModel @Inject constructor(
    private val repository: AudDistRepository
) : ViewModel() {

    val lecturers = liveData(Dispatchers.IO) {
        try {
            val list = repository.getLecturers()
            list.data = list.data.sortedBy { it.attributes.surname }
            emit(list)
        } catch (e: Exception) {
            emit(LecturersList(data = listOf()))
        }
    }

}
