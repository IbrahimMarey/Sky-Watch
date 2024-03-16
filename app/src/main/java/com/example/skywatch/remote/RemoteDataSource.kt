package com.example.skywatch.remote

import com.example.skywatch.models.WeatherPojo

class RemoteDataSource private constructor(private val service: ApiService):RemoteDataSourceInterface {
    override suspend fun getWeather(lat: String, lon: String): WeatherPojo {
        return service.getWeather(lat,lon)
    }

    companion object{
        private var remoteDataSource:RemoteDataSource? =null
        fun getInstance(service: ApiService):RemoteDataSource
        {
            return remoteDataSource?: synchronized(this){
                val instance = RemoteDataSource(service)
                remoteDataSource=instance
                instance
            }
        }
    }
}