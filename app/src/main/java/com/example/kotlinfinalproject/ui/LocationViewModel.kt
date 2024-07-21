package com.example.kotlinfinalproject.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class LocationViewModel(application: Application): AndroidViewModel(application) {

    val location: LiveData<String> = LocationModel(application.applicationContext)
}