package com.example.auddistandroid.ui.lecturers.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.auddistandroid.data.AudDistRepository
import com.example.auddistandroid.data.model.LecturerList
import com.example.auddistandroid.utils.ResponseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LecturersViewModel @Inject constructor(
    private val repository: AudDistRepository
) : ViewModel() {

    lateinit var responseStatus: ResponseStatus
    lateinit var authToken: String

    val lecturers = liveData(Dispatchers.IO) {
        val list = LecturerList(listOf())
        try {
            list.data = repository.getLecturers(authToken).data.sortedBy { it.attributes.surname }
            responseStatus = ResponseStatus.SUCCESS
        } catch (e: Exception) { // TODO изменить способ обратоки ошибок, возможно нужен MVI
            responseStatus = when (e) {
                is HttpException -> {
                    e.code() // TODO
                    ResponseStatus.UNAUTHORIZED // TODO HttpException возможно не подходит
                }
                is SocketTimeoutException -> ResponseStatus.NO_RESPONSE
                else -> ResponseStatus.UNKNOWN_ERROR
            }
        } finally {
            emit(list)
        }
    }

}
