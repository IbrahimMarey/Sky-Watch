package com.example.skywatch.location

import android.annotation.SuppressLint
import com.example.skywatch.models.status.LocationStatus
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.StateFlow

interface SkyWatchLocationManagerInterface
{
    val location: StateFlow<LocationStatus>
    @SuppressLint("MissingPermission")
    fun requestLocationData()
    fun requestLocationData(callBack:(LatLng)->Unit)
    fun isLocationEnabled():Boolean
}