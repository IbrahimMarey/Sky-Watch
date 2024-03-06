package com.example.skywatch.models.status

import com.google.android.gms.maps.model.LatLng

sealed class LocationStatus
{
    class Success(var latLng: LatLng):LocationStatus()
    class Failure(var errMsg: String):LocationStatus()
    object Loading:LocationStatus()
}