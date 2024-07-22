package com.example.kotlinfinalproject.ui
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import android.location.Geocoder
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume


class LocationModel(private var context: Context) {
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private val locationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100000).build()

    private var locationUpdated = false

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            lat = result.lastLocation!!.latitude
            long = result.lastLocation!!.longitude
            locationUpdated = true
        }
    }

    init {
        requestLocationUpdates()
    }

    private fun requestLocationUpdates() {
        locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

//    fun getLocation(): String {
//        if (!locationUpdated) {
//            // If location hasn't been updated yet, try to get last known location
//            val lastLocation = locationClient.lastLocation.result
//            if (lastLocation != null) {
//                lat = lastLocation.latitude
//                long = lastLocation.longitude
//            }
//        }
//        val address = getCityAndCountryFromLocation(lat,long)
//        return address
//    }
    suspend fun getLocation(): String = suspendCancellableCoroutine { continuation ->
        if (locationUpdated) {
            val address = getCityAndCountryFromLocation(lat, long)
            continuation.resume(address)
        } else {
            locationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    lat = location.latitude
                    long = location.longitude
                    val address = getCityAndCountryFromLocation(lat, long)
                    continuation.resume(address)
                } else {
                    continuation.resume("Unknown location")
                }
            }.addOnFailureListener { exception ->
                continuation.resume("Error: ${exception.message}")

            }
        }
    }

    fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
    }
    private fun getCityAndCountryFromLocation(lat : Double, long: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, long, 1)
        return if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            "${address.locality}, ${address.countryName}"
        } else {
            "Unknown location"
        }
    }

}
