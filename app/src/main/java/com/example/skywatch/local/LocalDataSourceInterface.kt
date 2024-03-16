package com.example.skywatch.local

import com.example.skywatch.models.AlertPojo
import com.example.skywatch.models.FavoritePojo

interface LocalDataSourceInterface {
    suspend fun insertFavLocation(favoritePojo: FavoritePojo):Long
    suspend fun delFavLocation(favoritePojo: FavoritePojo):Int
    suspend fun getAllFavLocations(): List<FavoritePojo>

    suspend fun insertAlertLocation(alertPojo: AlertPojo):Long
    suspend fun delAlertLocation(alertPojo: AlertPojo):Int
    suspend fun getAllAlertLocations(): List<AlertPojo>
    fun getAlertWithId(ID: String): AlertPojo
}