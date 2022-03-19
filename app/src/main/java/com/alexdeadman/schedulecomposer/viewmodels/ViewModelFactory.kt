package com.alexdeadman.schedulecomposer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexdeadman.schedulecomposer.service.AudDistApi
import kotlin.reflect.KClass

class ViewModelFactory(
    private val clazz: KClass<out AbstractViewModel>,
    private val audDistApi: AudDistApi
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return clazz.constructors.first().call(audDistApi) as T
    }
}