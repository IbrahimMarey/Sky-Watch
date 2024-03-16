package com.example.skywatch.Fakes

import com.example.skywatch.models.WeatherPojo
import com.example.skywatch.remote.RemoteDataSourceInterface

class FakeRemoteDataSource:RemoteDataSourceInterface {
    override suspend fun getWeather(lat: String, lon: String): WeatherPojo {
        return WeatherPojo()
    }
}