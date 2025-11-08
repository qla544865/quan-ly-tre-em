package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
object AppStateViewModel : ViewModel() {
    private val _currentApp = MutableLiveData<String>()
    val currentApp: LiveData<String> = _currentApp
    public var cur_app_value = ""

    fun updateCurrentApp(packageName: String) {
        _currentApp.postValue(packageName)
        cur_app_value = packageName
    }

}
