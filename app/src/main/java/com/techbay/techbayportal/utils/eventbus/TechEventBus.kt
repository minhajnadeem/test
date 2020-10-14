package com.techbay.techbayportal.utils.eventbus

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.techbay.techbayportal.networking.AddUserAddressData
import com.techbay.techbayportal.networking.AreasCities
import com.techbay.techbayportal.networking.Cities
import com.techbay.techbayportal.networking.GetUserAddressResponse

data class ShowLoadingEvent(val show: Boolean)
data class ShowSnackBarEvent(val message: String)
data class GetUserAddressEvent(val data: GetUserAddressResponse)
class AccessLocationPermissionEvent(val actionGranted: Any, val actionNotGranted: Any)
class GetLastKnownLocationEvent()
data class LastLocationEvent(val location: Location)
class LocationPermissionNotGrantedEvent()

//MapFragment Events
data class MapLocationUpdateEvent(val latLng: LatLng)
data class MapNavigateToAddressEvent(val latLng: LatLng)

//AddAddress Events
data class AddSetupCitiesEvent(val cities: List<Cities>)
data class AddSetupAreasEvent(val areasCities: List<AreasCities>)
data class AddNavigateToRecentAddress(val addressData: AddUserAddressData)

interface TechEventBus {

    fun register(any: Any) {
        if (BaseEventBus.eventBus.isRegistered(any).not()) {
            BaseEventBus.eventBus.register(any)
        }
    }

    fun unregister(any: Any) {
        BaseEventBus.eventBus.unregister(any)
    }

    fun postEvent(event: Any) {
        BaseEventBus.eventBus.post(event)
    }

    fun postStickyEvent(event: Any) {
        BaseEventBus.eventBus.postSticky(event)
    }

    fun <T> getStickyEvent(messageEvent: Class<T>): T {
        return BaseEventBus.eventBus.getStickyEvent(messageEvent)
    }

    fun removeSticky(event: Any) {
        BaseEventBus.eventBus.removeStickyEvent(event)
    }

    fun removeAllSticky() {
        BaseEventBus.eventBus.removeAllStickyEvents()
    }

    fun showLoading() {
        BaseEventBus.eventBus.post(ShowLoadingEvent(true))
    }

    fun hideLoading() {
        BaseEventBus.eventBus.post(ShowLoadingEvent(false))
    }

    fun showSnackBar(message: String) {
        BaseEventBus.eventBus.post(ShowSnackBarEvent(message))
    }
}