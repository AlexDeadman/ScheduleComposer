package com.alexdeadman.schedulecomposer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexdeadman.schedulecomposer.service.ScApi
import kotlin.reflect.KClass

class ViewModelFactory(
    private val scApi: ScApi
) : ViewModelProvider.NewInstanceFactory() {

    private lateinit var clazz: KClass<out AbstractViewModel>

    fun withClass(clazz: KClass<out AbstractViewModel>): ViewModelFactory {
        this.clazz = clazz
        return this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return clazz.constructors.first().call(scApi) as T
    }
}