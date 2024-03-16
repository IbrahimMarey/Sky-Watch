package com.example.skywatch.Fakes

import com.example.skywatch.local.LocalDataSourceInterface
import com.example.skywatch.models.AlertPojo
import com.example.skywatch.models.FavoritePojo

class FakeLocalDataSource :LocalDataSourceInterface{
    private val favList = mutableListOf<FavoritePojo>()
    override suspend fun insertFavLocation(favoritePojo: FavoritePojo): Long {
        favList.add(favoritePojo)
        return 1
    }

    override suspend fun delFavLocation(favoritePojo: FavoritePojo): Int {
        favList.remove(favList.first{it.address == favoritePojo.address})
        return 1
    }

    override suspend fun getAllFavLocations(): List<FavoritePojo> {
        val favoritePojo = FavoritePojo("ITI Ismailia",32.32344,32.23948)
        favList.add(favoritePojo)
        return favList
    }

    override suspend fun insertAlertLocation(alertPojo: AlertPojo): Long {
        TODO("Not yet implemented")
    }

    override suspend fun delAlertLocation(alertPojo: AlertPojo): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAlertLocations(): List<AlertPojo> {
        TODO("Not yet implemented")
    }

    override fun getAlertWithId(ID: String): AlertPojo {
        TODO("Not yet implemented")
    }
}