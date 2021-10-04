package com.example.auddistandroid.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "<-- Тестируй в другую сторону"
    }
    val text: LiveData<String> = _text
}