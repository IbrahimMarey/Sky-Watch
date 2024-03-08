package com.example.skywatch.views.ui.map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywatch.models.FavoritePojo
import com.example.skywatch.models.repos.WeatherRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(private val weatherRepo: WeatherRepo):ViewModel()
{


    fun insertFavLocation(favoritePojo: FavoritePojo)
    {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepo.insertFavLocation(favoritePojo)
        }
    }
}