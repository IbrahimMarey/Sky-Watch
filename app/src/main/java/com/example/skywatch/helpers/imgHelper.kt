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