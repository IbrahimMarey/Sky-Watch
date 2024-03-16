package com.example.skywatch.models.status

import com.example.skywatch.models.AlertPojo

sealed class AlertStatus 
{
    class Success(var alertList:List<AlertPojo>):AlertStatus()
    class Failure(var errMsg:String):AlertStatus()
    object Loading : AlertStatus()
}