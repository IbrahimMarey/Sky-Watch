package com.example.skywatch.views.ui.alert.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywatch.models.AlertPojo
import com.example.skywatch.models.repos.WeatherRepo
import com.example.skywatch.models.status.AlertStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel(private val weatherRepo: WeatherRepo):ViewModel() {

    var alertList : MutableStateFlow<AlertStatus> = MutableStateFlow<AlertStatus>(AlertStatus.Loading)
    var alerts :StateFlow<AlertStatus> = alertList

    init {
        getAlerts()
    }

    fun getAlerts()
    {
        viewModelScope.launch {
            weatherRepo.getAllAlertLocations()
                .catch {
                    alertList.value = AlertStatus.Failure(it.message.toString())

                }.collect{
                    alertList.value = AlertStatus.Success(it)
                }
        }
    }

    fun addAlert(alertPojo: AlertPojo)
    {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepo.insertAlertLocation(alertPojo)
            getAlerts()
        }
    }

    fun delAlert(alertPojo: AlertPojo)
    {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepo.delAlertLocation(alertPojo)
            getAlerts()
        }
    }

}