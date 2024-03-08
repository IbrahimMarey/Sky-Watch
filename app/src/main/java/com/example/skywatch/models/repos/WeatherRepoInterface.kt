package com.example.skywatch.models.repos

import com.example.skywatch.models.FavoritePojo
import com.example.skywatch.models.WeatherPojo
import kotlinx.coroutines.flow.Flow

interface WeatherRepoInterface
{
    suspend fun getWeather(lat :String, lon:String): Flow<WeatherPojo>
    suspend fun getAllFavLocation(): Flow<List<FavoritePojo>>
    suspend fun insertFavLocation(favoritePojo: FavoritePojo):Long
    suspend fun delFavLocation(favoritePojo: FavoritePojo):Int
}