package com.example.skywatch.views.ui.map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skywatch.models.repos.WeatherRepo

class MapViewModelFactory(private val weatherRepo: WeatherRepo): ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapViewModel::class.java))
        {
            MapViewModel(weatherRepo) as T
        }else
        {
            throw IllegalArgumentException("Not Found")
        }
    }
}