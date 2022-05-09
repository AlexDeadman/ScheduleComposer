package com.alexdeadman.schedulecomposer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexdeadman.schedulecomposer.service.ScApi

class ViewModelFactory(
    private val scApi: ScApi,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = modelClass.kotlin.constructors
        .first()
        .call(scApi)
}