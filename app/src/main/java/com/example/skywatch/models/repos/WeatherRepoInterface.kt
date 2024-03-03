package com.example.skywatch.models.repos

import com.example.skywatch.models.WeatherPojo

interface WeatherRepoInterface
{
    suspend fun getWeather(lat :String, lon:String): WeatherPojo
}