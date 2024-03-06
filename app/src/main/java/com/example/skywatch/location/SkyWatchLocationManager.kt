package com.example.skywatch.location

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.util.Log
import com.example.skywatch.Constants
import com.example.skywatch.models.status.LocationStatus
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SkyWatchLocationManager private constructor(private val application: Application):SkyWatchLocationManagerInterface
{
    private val _location = MutableStateFlow<LocationStatus>(LocationStatus.Loading)
    override val location = _location.asStateFlow()

    private lateinit var locationCallback: LocationCallback
    private val fusedClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(application)
    }
    companion object{
        private var locationInstance:SkyWatchLocationManager? = null
        fun getInstance(application: Application):SkyWatchLocationManager
        {
            return locationInstance?: synchronized(this){
                var instance = SkyWatchLocationManager(application)
                locationInstance = instance
                instance
            }
        }
    }
    @SuppressLint("MissingPermission")
    override fun requestLocationData() {
        val tokenSource = CancellationTokenSource()
        val token = tokenSource.token
        fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,token).addOnSuccessListener {
            val isEmitted =
                _location.tryEmit(LocationStatus.Success(LatLng(it.latitude, it.longitude)))
            Log.d(Constants.TAG, "onLocationResult: $isEmitted")
        }
    }

    @SuppressLint("MissingPermission")
    override fun requestLocationData(callBack: (LatLng) -> Unit)
    {
        val tokenSource = CancellationTokenSource()
        val token = tokenSource.token
        fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,token).addOnSuccessListener {
            callBack(LatLng(it.latitude, it.longitude))
        }
    }

    override fun isLocationEnabled(): Boolean {
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

}