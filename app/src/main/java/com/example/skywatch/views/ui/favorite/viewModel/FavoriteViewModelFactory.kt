package com.example.skywatch.views.ui.favorite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skywatch.models.repos.WeatherRepo

class FavoriteViewModelFactory(private val weatherRepo: WeatherRepo) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java))
        {
            FavoriteViewModel(weatherRepo) as T
        }else
        {
            throw IllegalArgumentException("Not Found")
        }
    }
}