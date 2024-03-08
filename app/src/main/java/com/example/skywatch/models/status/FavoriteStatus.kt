package com.example.skywatch.models.status

import com.example.skywatch.models.FavoritePojo
import com.example.skywatch.models.WeatherPojo

sealed class FavoriteStatus
{
    class Success(var favoriteList: List<FavoritePojo>):FavoriteStatus()
    class Failure(var errMsg:String):FavoriteStatus()
    object Loading:FavoriteStatus()
}