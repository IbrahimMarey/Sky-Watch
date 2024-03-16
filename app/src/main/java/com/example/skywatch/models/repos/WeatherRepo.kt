package com.example.skywatch.models.repos

import com.example.skywatch.local.LocalDataSource
import com.example.skywatch.local.LocalDataSourceInterface
import com.example.skywatch.local.SkyWatchDatabase
import com.example.skywatch.models.AlertPojo
import com.example.skywatch.models.FavoritePojo
import com.example.skywatch.models.WeatherPojo
import com.example.skywatch.remote.RemoteDataSource
import com.example.skywatch.remote.RemoteDataSourceInterface
import com.example.skywatch.remote.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherRepo private constructor(private val remoteDataSource: RemoteDataSourceInterface,private val localDataSource: LocalDataSourceInterface):WeatherRepoInterface{
    companion object{
        private var weatherRepo:WeatherRepo? = null
        fun getInstance(remoteDataSource: RemoteDataSourceInterface,localDataSource: LocalDataSourceInterface):WeatherRepo
        {
            return weatherRepo?: synchronized(this)
            {
                var  instance = WeatherRepo(remoteDataSource,localDataSource)
                weatherRepo=instance
                instance
            }
        }
    }
    override suspend fun getWeather(lat: String, lon: String)=flow<WeatherPojo> {
        emit( remoteDataSource.getWeather(lat,lon))
    }.flowOn(Dispatchers.IO)
    override suspend fun getAllFavLocation()= flow<List<FavoritePojo>> {
        emit( localDataSource.getAllFavLocations())
    }.flowOn(Dispatchers.IO)

    override suspend fun insertFavLocation(favoritePojo: FavoritePojo): Long {
        return localDataSource.insertFavLocation(favoritePojo)
    }

    override suspend fun delFavLocation(favoritePojo: FavoritePojo): Int {
        return localDataSource.delFavLocation(favoritePojo)
    }

    override suspend fun insertAlertLocation(alertPojo: AlertPojo): Long {
        return localDataSource.insertAlertLocation(alertPojo)
    }

    override suspend fun delAlertLocation(alertPojo: AlertPojo): Int {
        return localDataSource.delAlertLocation(alertPojo)
    }

    override suspend fun getAllAlertLocations()= flow<List<AlertPojo>> {
         emit(localDataSource.getAllAlertLocations())
    }.flowOn(Dispatchers.IO)

    override fun getAlertWithId(entryId: String): AlertPojo {
        return localDataSource.getAlertWithId(entryId)
    }


}