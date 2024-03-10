package com.example.skywatch.models.repos

import com.example.skywatch.local.SkyWatchDatabase
import com.example.skywatch.models.AlertPojo
import com.example.skywatch.models.FavoritePojo
import com.example.skywatch.models.WeatherPojo
import com.example.skywatch.remote.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherRepo private constructor(private val retrofitHelper:RetrofitHelper,private val skyWatchDatabase: SkyWatchDatabase):WeatherRepoInterface{
    companion object{
        private var weatherRepo:WeatherRepo? = null
        fun getInstance(retrofitHelper:RetrofitHelper,skyWatchDatabase: SkyWatchDatabase):WeatherRepo
        {
            return weatherRepo?: synchronized(this)
            {
                var  instance = WeatherRepo(retrofitHelper,skyWatchDatabase)
                weatherRepo=instance
                instance
            }
        }
    }
    override suspend fun getWeather(lat: String, lon: String)=flow<WeatherPojo> {
        emit( retrofitHelper.service.getWeather(lat,lon))
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllFavLocation()= flow<List<FavoritePojo>> {
        emit( skyWatchDatabase.SkyWatchDao().getAllFavLocations())
    }.flowOn(Dispatchers.IO)

    override suspend fun insertFavLocation(favoritePojo: FavoritePojo): Long {
        return skyWatchDatabase.SkyWatchDao().insertFavLocation(favoritePojo)
    }

    override suspend fun delFavLocation(favoritePojo: FavoritePojo): Int {
        return skyWatchDatabase.SkyWatchDao().delFavLocation(favoritePojo)
    }

    override suspend fun insertAlertLocation(alertPojo: AlertPojo): Long {
        return skyWatchDatabase.SkyWatchDao().insertAlertLocation(alertPojo)
    }

    override suspend fun delAlertLocation(alertPojo: AlertPojo): Int {
        return skyWatchDatabase.SkyWatchDao().delAlertLocation(alertPojo)
    }

    override suspend fun getAllAlertLocations(): List<AlertPojo> {
        return skyWatchDatabase.SkyWatchDao().getAllAlertLocations()
    }


}