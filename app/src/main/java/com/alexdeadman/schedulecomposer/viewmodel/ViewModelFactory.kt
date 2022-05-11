package com.alexdeadman.schedulecomposer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexdeadman.schedulecomposer.service.Api
import com.google.gson.Gson

class ViewModelFactory(
    private val gson: Gson,
    private val api: Api,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = modelClass.kotlin.constructors
        .first()
        .call(gson, api)
}