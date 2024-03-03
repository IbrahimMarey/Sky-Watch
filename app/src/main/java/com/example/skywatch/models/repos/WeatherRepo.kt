package com.example.skywatch.models.repos

import com.example.skywatch.models.WeatherPojo
import com.example.skywatch.remote.RetrofitHelper

class WeatherRepo private constructor(private val retrofitHelper:RetrofitHelper):WeatherRepoInterface{
    companion object{
        private var weatherRepo:WeatherRepo? = null
        fun getInstance(retrofitHelper:RetrofitHelper):WeatherRepo
        {
            return weatherRepo?: synchronized(this)
            {
                var  instance = WeatherRepo(retrofitHelper)
                weatherRepo=instance
                instance
            }
        }
    }
    override suspend fun getWeather(lat: String, lon: String): WeatherPojo {
        return retrofitHelper.service.getWeather(lat,lon)
    }
}