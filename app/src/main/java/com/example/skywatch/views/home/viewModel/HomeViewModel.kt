package com.example.skywatch.views.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywatch.Constants
import com.example.skywatch.models.WeatherPojo
import com.example.skywatch.models.repos.WeatherRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val weatherRepo: WeatherRepo) :ViewModel()
{
    private val weather:MutableLiveData<WeatherPojo> = MutableLiveData<WeatherPojo>()
    val weatherData:LiveData<WeatherPojo> = weather
    init {
        //getWeather()
    }
    fun getWeather(lat:String,lon:String)
    {
        viewModelScope.launch(Dispatchers.IO) {

            var testWeather= weatherRepo.getWeather("30.1771","31.9535")
            weather.postValue(weatherRepo.getWeather(lat,lon))
            Log.i(Constants.TAG, "getWeather:${testWeather.current?.sunrise} ")
            Log.i(Constants.TAG, "getWeather:${testWeather.hourly?.size} ")
        }
    }
}