package com.alexdeadman.schedulecomposer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexdeadman.schedulecomposer.service.ScApi
import kotlin.reflect.KClass

class ViewModelFactory(
    private val scApi: ScApi,
) : ViewModelProvider.NewInstanceFactory() {

    private lateinit var clazz: KClass<out AbstractEntityViewModel>

    fun withClass(clazz: KClass<out AbstractEntityViewModel>): ViewModelFactory {
        this.clazz = clazz
        return this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return clazz.constructors.first().call(scApi) as T
    }
}