package com.example.skywatch.views.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skywatch.models.repos.WeatherRepo

class HomeViewModelFactory(private val weatherRepo: WeatherRepo) :ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java))
        {
            HomeViewModel(weatherRepo) as T
        }else
        {
            throw IllegalArgumentException("Not Found")
        }
    }
}