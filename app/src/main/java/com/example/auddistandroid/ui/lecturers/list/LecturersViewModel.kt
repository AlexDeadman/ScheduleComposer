package com.example.auddistandroid.ui.lecturers.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.auddistandroid.data.AudDistRepository
import com.example.auddistandroid.data.model.LecturersList
import com.example.auddistandroid.ui.QueryStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LecturersViewModel @Inject constructor(
    private val repository: AudDistRepository
) : ViewModel() {

    lateinit var queryStatus: QueryStatus
    lateinit var authToken: String

    val lecturers = liveData(Dispatchers.IO) {
        val list = LecturersList(listOf())
        try {
            list.data = repository.getLecturers(authToken).data.sortedBy { it.attributes.surname }
            queryStatus = QueryStatus.SUCCESS
        } catch (e: Exception) { // TODO изменить способ обратоки ошибок, возможно нужен MVI
            queryStatus = when (e) {
                is HttpException -> QueryStatus.UNAUTHORIZED // TODO HttpException возможно не подходит
                is SocketTimeoutException -> QueryStatus.NO_RESPONSE
                else -> QueryStatus.UNKNOWN_ERROR
            }
        } finally {
            emit(list)
        }
    }

}
