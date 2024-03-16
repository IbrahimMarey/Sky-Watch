package com.example.skywatch.helpers

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.skywatch.R

fun ImageView.setImageFromNetworkUrL(urlString: String){
    Glide.with(this)
        .load(urlString)
        .into(this)
}

fun ImageView.setImageFromWeatherIconId(iconId: String){

    val urlString = "https://openweathermap.org/img/wn/$iconId.png"
    Glide.with(this)
        .load(urlString)
        .into(this)
}
fun ImageView.setImageFromWeatherIconId2x(iconId: String){

    val urlString = "https://openweathermap.org/img/wn/$iconId@2x.png"
    Glide.with(this)
        .load(urlString)
        .error(R.drawable.ic_wifi_off)
        .into(this)
}
fun ImageView.setImageFromWeatherIconId4x(iconId: String){

    val urlString = "https://openweathermap.org/img/wn/$iconId@4x.png"
    Glide.with(this)
        .load(urlString)
        .error(R.drawable.ic_wifi_off)
        .placeholder(R.drawable.ic_wifi_off)
        .into(this)
}

fun getWeatherImg(img : String):Int
{
    when(img)
    {
        "01d"->return R.drawable.sun
        "01n"->return R.drawable.moon
        "02d"->return R.drawable.cloudy
        "02n"->return R.drawable.cloudy_night
        "03d","03n"-> return R.drawable.light_cloud
        "04d","04n"->return R.drawable.dark_cloud
        "10d"->return R.drawable.rainy_sun
        "10n"->return R.drawable.raining_moon
        else -> return R.drawable.cloud_sun
    }
}