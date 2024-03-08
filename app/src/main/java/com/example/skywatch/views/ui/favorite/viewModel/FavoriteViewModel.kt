package com.example.skywatch.views.ui.favorite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywatch.models.FavoritePojo
import com.example.skywatch.models.repos.WeatherRepo
import com.example.skywatch.models.status.FavoriteStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(private val weatherRepo: WeatherRepo) :ViewModel()
{
    private val favList : MutableStateFlow<FavoriteStatus> = MutableStateFlow<FavoriteStatus>(FavoriteStatus.Loading)
    val favoriteList:StateFlow<FavoriteStatus> = favList

    init {
        getFavorites()
    }

    fun getFavorites()
    {
        viewModelScope.launch {
            weatherRepo.getAllFavLocation()
                .catch {
                    favList.value = FavoriteStatus.Failure(it.message.toString())
                }.collect{
                    favList.value = FavoriteStatus.Success(it)
                }
        }
    }

    fun delFromFavorites(favoritePojo: FavoritePojo)
    {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepo.delFavLocation(favoritePojo)
            getFavorites()
        }
    }
}
