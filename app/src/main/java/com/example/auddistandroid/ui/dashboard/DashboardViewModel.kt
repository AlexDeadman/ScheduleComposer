//package com.example.auddistandroid.ui.dashboard
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.liveData
//import com.example.auddistandroid.data.AudDistRepository
//import com.example.auddistandroid.data.model.ScheduleList
//import com.example.auddistandroid.utils.ResponseStatus
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import retrofit2.HttpException
//import java.net.SocketTimeoutException
//import javax.inject.Inject
//
//@HiltViewModel
//class DashboardViewModel @Inject constructor(
//    private val repository: AudDistRepository
//) : ViewModel() {
//
//    lateinit var responseStatus: ResponseStatus
//    lateinit var authToken: String
//
//    val schedules = liveData(Dispatchers.IO) {
//        val list = ScheduleList(listOf())
//        try {
//            list.data = repository.getSchedules(authToken).data.sortedBy { it.attributes.semester }
//            responseStatus = ResponseStatus.SUCCESS
//        } catch (e: Exception) {
//            responseStatus = when (e) {
//                is HttpException -> {
//                    e.code() // TODO
//                    ResponseStatus.UNAUTHORIZED
//                }
//                is SocketTimeoutException -> ResponseStatus.NO_RESPONSE
//                else -> ResponseStatus.UNKNOWN_ERROR
//            }
//        } finally {
//            emit(list)
//        }
//    }
//
//}