package com.example.skywatch.helpers

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import java.util.Locale

fun getAddressEnglish(context: Context, lat: Double?, lon: Double?):String{
    var address:MutableList<Address>?
    val geocoder= Geocoder(context)
    address =geocoder.getFromLocation(lat?:0.0,lon?:0.0,1)
    if (address?.isEmpty()==true) {
        return "Unkown location"
    } else if (address?.get(0)?.countryName.isNullOrEmpty()) {
        return "Unkown Country"
    } else if (address?.get(0)?.adminArea.isNullOrEmpty()) {
        return address?.get(0)?.countryName.toString()
    } else{
        return address?.get(0)?.countryName.toString()+", "+address?.get(0)?.adminArea+", "+address?.get(0)?.locality
    }
}

fun getMarkerAddress(context: Context, lat: Double, lon: Double):String{
    var address:MutableList<Address>?
    val geocoder= Geocoder(context)
    address =geocoder.getFromLocation(lat,lon,1)
    /*if (address?.isEmpty()==true) {
        return "Unkown location"
    } else if (address?.get(0)?.countryName.isNullOrEmpty()) {
        return "Unkown Country"
    } else if (address?.get(0)?.adminArea.isNullOrEmpty()) {
        return address?.get(0)?.countryName.toString()
    } else if (!address?.get(0)?.adminArea.isNullOrEmpty() && !address?.get(0)?.countryName.isNullOrEmpty() && !address?.get(0)?.subAdminArea.isNullOrEmpty()){
        return address?.get(0)?.countryName.toString()+", "+address?.get(0)?.adminArea+", "+address?.get(0)?.subAdminArea
    }*/
    return address?.get(0)?.countryName.toString()+", "+address?.get(0)?.adminArea+", "+address?.get(0)?.subAdminArea
}

fun formatAddressToCity(address: Address?): String {
    return address?.let {
        if (it.subAdminArea != null) {
            "${it.adminArea}, ${it.subAdminArea}"
        } else {
            it.adminArea
        }
    }?:""
}


fun getAddress(context: Context, long: Double, lat:Double, locale: Locale, onResult: (Address?) -> Unit) {
    var address: Address?
    val geocoder = Geocoder(context, locale)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geocoder.getFromLocation(
            lat, long, 1
        ) {
            address = it[0]
            onResult(address)
        }
    } else {
        val addressList = geocoder.getFromLocation(lat, long, 1)
        if(!addressList.isNullOrEmpty()){
            address = geocoder.getFromLocation(lat, long, 1)?.get(0)
            onResult(address)
        }else{
            onResult(null)
        }

    }
}