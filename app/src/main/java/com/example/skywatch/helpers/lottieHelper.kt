package com.example.skywatch.helpers

import com.example.skywatch.R

fun getWeatherLottie(img : String):Int
{
    when(img)
    {
        "01d"->return R.raw.sunny_one
        "01n"->return R.raw.moon_sleep
        "02d"->return R.raw.cloud_sun
        "02n"->return R.raw.cloud_moon
        "03d","03n","04d","04n"-> return R.raw.clouds
        else -> return R.raw.cloudy_test
    }
}