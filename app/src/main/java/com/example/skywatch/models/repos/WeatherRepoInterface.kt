package com.example.skywatch.models.repos

import androidx.room.Delete
import androidx.room.Query
import com.example.skywatch.models.AlertPojo
import com.example.skywatch.models.FavoritePojo
import com.example.skywatch.models.WeatherPojo
import kotlinx.coroutines.flow.Flow

interface WeatherRepoInterface
{
    suspend fun getWeather(lat :String, lon:String): Flow<WeatherPojo>
    suspend fun getAllFavLocation(): Flow<List<FavoritePojo>>
    suspend fun insertFavLocation(favoritePojo: FavoritePojo):Long
    suspend fun delFavLocation(favoritePojo: FavoritePojo):Int
    suspend fun insertAlertLocation(alertPojo: AlertPojo):Long
    suspend fun delAlertLocation(alertPojo: AlertPojo):Int
    suspend fun getAllAlertLocations(): Flow<List<AlertPojo>>

    fun getAlertWithId(entryId: String): AlertPojo
}