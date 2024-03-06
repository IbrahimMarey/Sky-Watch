package com.example.skywatch.models.status

import com.example.skywatch.models.WeatherPojo

sealed class HomeStatus
{
    class Success(var weatherPojo: WeatherPojo):HomeStatus()
    class Failure(var errMsg:String):HomeStatus()
    object Loading:HomeStatus()
}