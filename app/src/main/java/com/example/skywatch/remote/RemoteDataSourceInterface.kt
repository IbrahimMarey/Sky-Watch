package com.example.skywatch.remote

import com.example.skywatch.Constants
import com.example.skywatch.models.WeatherPojo
import retrofit2.http.Query

interface RemoteDataSourceInterface
{
    suspend fun getWeather(lat :String, lon:String,): WeatherPojo
}