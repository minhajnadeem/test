package com.techbay.techbayportal.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.techbay.techbayportal.utils.eventbus.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MapFragmentViewModel : ViewModel(), TechEventBus {

    private var currLatLng: LatLng? = null

    fun start() {
        register(this)
    }

    fun stop() {
        unregister(this)
    }

    fun onMapReady() {
        postEvent(
            AccessLocationPermissionEvent(
                GetLastKnownLocationEvent(),
                LocationPermissionNotGrantedEvent()
            )
        )
    }

    fun currentLocationClicked() {
        postEvent(GetLastKnownLocationEvent())
    }

    fun addAddressClicked() {
        currLatLng?.apply {
            postEvent(MapNavigateToAddressEvent(this))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: LastLocationEvent) {
        currLatLng = LatLng(event.location.latitude, event.location.longitude)
        currLatLng?.apply {
            postEvent(MapLocationUpdateEvent(this))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: LocationPermissionNotGrantedEvent) {
        showSnackBar("Location permission not granted")
    }
}