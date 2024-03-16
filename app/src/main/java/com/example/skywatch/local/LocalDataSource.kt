package com.example.skywatch.local

import com.example.skywatch.models.AlertPojo
import com.example.skywatch.models.FavoritePojo

class LocalDataSource private constructor(private val skyWatchDao: SkyWatchDao):LocalDataSourceInterface {
    override suspend fun insertFavLocation(favoritePojo: FavoritePojo): Long {
        return skyWatchDao.insertFavLocation(favoritePojo)
    }

    override suspend fun delFavLocation(favoritePojo: FavoritePojo): Int {
        return skyWatchDao.delFavLocation(favoritePojo)
    }

    override suspend fun getAllFavLocations(): List<FavoritePojo> {
        return skyWatchDao.getAllFavLocations()
    }

    override suspend fun insertAlertLocation(alertPojo: AlertPojo): Long {
        return skyWatchDao.insertAlertLocation(alertPojo)
    }

    override suspend fun delAlertLocation(alertPojo: AlertPojo): Int {
        return skyWatchDao.delAlertLocation(alertPojo)
    }

    override suspend fun getAllAlertLocations(): List<AlertPojo> {
        return skyWatchDao.getAllAlertLocations()
    }

    override fun getAlertWithId(id: String): AlertPojo {
        return skyWatchDao.getAlertWithId(id)
    }

    companion object{
        private var localDataSource:LocalDataSource? =null

        fun getInstance(skyWatchDao: SkyWatchDao):LocalDataSource
        {
            return localDataSource?: synchronized(this)
            {
                var instance = LocalDataSource(skyWatchDao)
                localDataSource=instance
                instance
            }
        }
    }
}