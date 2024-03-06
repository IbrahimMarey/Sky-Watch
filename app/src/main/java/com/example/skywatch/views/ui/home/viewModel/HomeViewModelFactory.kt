package com.example.skywatch.views.ui.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skywatch.location.SkyWatchLocationManagerInterface
import com.example.skywatch.models.repos.WeatherRepo

class HomeViewModelFactory(private val weatherRepo: WeatherRepo, private val skyWatchLocationManagerInterface: SkyWatchLocationManagerInterface) :ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java))
        {
            HomeViewModel(weatherRepo,skyWatchLocationManagerInterface) as T
        }else
        {
            throw IllegalArgumentException("Not Found")
        }
    }
}