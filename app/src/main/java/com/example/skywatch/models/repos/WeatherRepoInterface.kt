package com.example.skywatch.models.repos

import com.example.skywatch.models.WeatherPojo
import kotlinx.coroutines.flow.Flow

interface WeatherRepoInterface
{
    suspend fun getWeather(lat :String, lon:String): Flow<WeatherPojo>
}