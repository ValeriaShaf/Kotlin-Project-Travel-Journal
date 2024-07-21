package com.example.kotlinfinalproject.ui
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

import android.os.Looper
import androidx.lifecycle.LiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LocationModel(private val context: Context): LiveData<String>() {
    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.IO)
    private val geocoder by lazy { Geocoder(context) }
    private val clientLocation : FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,100000).build()
    private val locationCallback = object : LocationCallback(){
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onLocationResult(result: LocationResult){
            Log.d("Location Result", "in onLocationResult function")
            result.lastLocation?.let {
//                coroutineScope.launch {
//                    val address = geocoder.getFromLocation(it.latitude,it.longitude,1,
//                        object : Geocoder.GeocodeListener{
//                            override fun onGeocode(address: MutableList<android.location.Address>) {
//                                postValue(address.first().getAddressLine(0))
//                            }
//                        })
//
//                }
                Log.d("Location Result", "${result.lastLocation!!.latitude}, ${result.lastLocation!!.longitude}")
                postValue("${result.lastLocation!!.latitude}, ${result.lastLocation!!.longitude}")}
        }
    }
    override fun onActive() {
        super.onActive()
        try{
            if (ActivityCompat.checkSelfPermission(
                    this.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this.context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                clientLocation.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())}
                return
            }catch (e: SecurityException){
                Log.d("location exception", "${e.message.toString()}")
            }

    }
    override fun onInactive() {
        clientLocation.removeLocationUpdates(locationCallback)
    }
}