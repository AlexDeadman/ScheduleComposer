package com.example.auddistandroid.ui.lecturers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.auddistandroid.data.AudDistRepository
import com.example.auddistandroid.data.model.LecturersList
import com.example.auddistandroid.ui.QueryStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LecturersViewModel @Inject constructor(
    private val repository: AudDistRepository
) : ViewModel() {

    lateinit var queryStatus: QueryStatus

    val lecturers = liveData(Dispatchers.IO) {
        val list = LecturersList(listOf())
        try {
            list.data = repository.getLecturers().data.sortedBy { it.attributes.surname }
            queryStatus = QueryStatus.SUCCESS
        } catch (e: Exception) {
            queryStatus = when (e) {
                is ConnectException -> QueryStatus.NO_INTERNET
                is SocketTimeoutException -> QueryStatus.NO_RESPONSE
                else -> QueryStatus.UNKNOWN_ERROR
            }
        } finally {
            emit(list)
        }
    }

}
