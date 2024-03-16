package com.example.skywatch.views.ui.alert.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skywatch.models.repos.WeatherRepo

class AlertViewModelFactory(private val weatherRepo: WeatherRepo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java))
        {
            AlertViewModel(weatherRepo) as T
        }else{
            throw IllegalArgumentException("Not Found")
        }
    }
}