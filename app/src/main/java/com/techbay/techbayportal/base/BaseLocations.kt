package com.techbay.techbayportal.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.techbay.techbayportal.utils.eventbus.GetLastKnownLocationEvent
import com.techbay.techbayportal.utils.eventbus.LastLocationEvent
import com.techbay.techbayportal.utils.eventbus.TechEventBus
import org.greenrobot.eventbus.Subscribe

class BaseLocations(private val activity: AppCompatActivity) : LifecycleObserver, TechEventBus,
    LocationCallback() {

    companion object {
        const val MAP_ZOOM = 15f
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        register(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stop() {
        unregister(this)
    }

    @SuppressLint("MissingPermission")
    @Subscribe
    fun onEvent(event: GetLastKnownLocationEvent) {
        val task = fusedLocationClient.lastLocation
        task.addOnSuccessListener {
            postEvent(LastLocationEvent(it))
        }
    }
}