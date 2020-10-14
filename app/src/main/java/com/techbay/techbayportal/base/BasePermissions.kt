package com.techbay.techbayportal.base

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.techbay.techbayportal.utils.eventbus.AccessLocationPermissionEvent
import com.techbay.techbayportal.utils.eventbus.TechEventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

class BasePermissions(private val activity: AppCompatActivity) : LifecycleObserver, TechEventBus {
    private val permissionGranted = PackageManager.PERMISSION_GRANTED

    //location
    private val accessCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
    private val accessFineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private val accessLocationPermissions = listOf(
        accessFineLocation, accessCoarseLocation
    )

    private fun checkCoarsePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            accessCoarseLocation
        ) == permissionGranted
    }

    private fun checkFinePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            accessFineLocation
        ) == permissionGranted
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun registerSubscriber() {
        register(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregisterSubscriber() {
        unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: AccessLocationPermissionEvent) {
        if (checkFinePermission() && checkCoarsePermission()) {
            postEvent(event.actionGranted)
        } else {
            val accessLocationPermission =
                activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    val granted = it.values.contains(false).not()
                    Timber.d("permissions granted: $granted $it")
                    if (granted) {
                        postEvent(event.actionGranted)
                    } else {
                        postEvent(event.actionNotGranted)
                    }
                }
            accessLocationPermission.launch(accessLocationPermissions.toTypedArray())
        }
    }
}