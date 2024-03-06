package com.example.skywatch.views.ui.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywatch.Constants
import com.example.skywatch.location.SkyWatchLocationManagerInterface
import com.example.skywatch.models.WeatherPojo
import com.example.skywatch.models.repos.WeatherRepo
import com.example.skywatch.models.status.HomeStatus
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val weatherRepo: WeatherRepo , private val skyWatchLocationManagerInterface: SkyWatchLocationManagerInterface) :ViewModel()
{
    private val weather:MutableStateFlow<HomeStatus> = MutableStateFlow<HomeStatus>(HomeStatus.Loading)
    val weatherData:StateFlow<HomeStatus> = weather
    val location = skyWatchLocationManagerInterface.location
    init {
        //getWeather()
    }
    fun getWeather(lat:String,lon:String)
    {
        Log.i(Constants.TAG, "getWeather: ${lat} ${lon}")
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepo.getWeather(lat,lon).catch {
                weather.value =HomeStatus.Failure(it.message.toString())
            }.collect{
                weather.value=HomeStatus.Success(it)
            }
        }
    }

    fun isLocationEnabled(): Boolean{
        return skyWatchLocationManagerInterface.isLocationEnabled()
    }

    fun requestLocation(callBack: (LatLng) ->Unit){
        skyWatchLocationManagerInterface.requestLocationData(callBack)
    }
}