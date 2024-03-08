package com.example.skywatch.helpers

import android.content.Context
import android.location.Address
import android.location.Geocoder

fun getAddressEnglish(context: Context, lat: Double?, lon: Double?):String{
    var address:MutableList<Address>?
    val geocoder= Geocoder(context)
    address =geocoder.getFromLocation(lat!!,lon!!,1)
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