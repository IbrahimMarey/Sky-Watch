package com.example.skywatch.models.repos

import com.example.skywatch.models.WeatherPojo
import com.example.skywatch.remote.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

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
    override suspend fun getWeather(lat: String, lon: String)=flow<WeatherPojo> {
        emit( retrofitHelper.service.getWeather(lat,lon))
    }.flowOn(Dispatchers.IO)
}