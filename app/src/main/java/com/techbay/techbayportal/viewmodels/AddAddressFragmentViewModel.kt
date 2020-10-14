package com.techbay.techbayportal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.techbay.techbayportal.networking.*
import com.techbay.techbayportal.utils.eventbus.*

class AddAddressFragmentViewModel : ViewModel(), TechEventBus {
    private lateinit var latLng: LatLng
    private lateinit var userAddressEvent: GetUserAddressEvent
    private lateinit var listCities: List<Cities>
    private lateinit var listAreas: List<AreasCities>
    private lateinit var listSpinnerAreas: List<AreasCities>

    //live data
    private var _selectedCityId = MutableLiveData<Int>()
    val selectedCityId: LiveData<Int> get() = _selectedCityId
    private var _selectedAreaId = MutableLiveData<Int>()
    val selectedAreaId: LiveData<Int> get() = _selectedAreaId
    private var _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double> get() = _latitude
    private var _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double> get() = _longitude

    //listener
    private val listener = object : ApiListener<AddUserAddressResponse> {
        override fun onSuccess(body: AddUserAddressResponse?) {
            hideLoading()
            body?.apply {
                if (status) {
                    postEvent(AddNavigateToRecentAddress(data))
                } else {
                    showSnackBar(errors.first())
                }
            }
        }

        override fun onFailure(error: Throwable) {
            hideLoading()
            error.message?.let { showSnackBar(it) }
        }
    }

    fun start(currLatLng: LatLng) {
        latLng = currLatLng
        _latitude.value = latLng.latitude
        _longitude.value = latLng.longitude
        userAddressEvent = getStickyEvent(GetUserAddressEvent::class.java)
        listCities = userAddressEvent.data.data.cities
        listAreas = userAddressEvent.data.data.areasOfCities
        postEvent(AddSetupCitiesEvent(listCities))
    }

    fun stop() {

    }

    fun spinnerCitiesSelected(position: Int) {
        val city = listCities[position]
        _selectedCityId.value = city.cityId
        val filteredList = listAreas.filter { it.cityId == city.cityId }
        listSpinnerAreas = filteredList
        postEvent(AddSetupAreasEvent(filteredList))
    }

    fun spinnerAreasSelected(position: Int) {
        val area = listSpinnerAreas[position]
        _selectedAreaId.value = area.areaId
    }

    fun btnAddClicked(
        building: String,
        apt: String,
        street: String,
        city: String,
        area: String,
        device: String,
        lang: String,
        lat: String,
        lan: String
    ) {
        showLoading()
        Repository.addUserAddress(
            AddUserAddressRequest(
                lat.toDouble(),
                lan.toDouble(),
                building,
                apt,
                street,
                area.toInt(),
                city.toInt(),
                device.toInt(),
                lang
            ), listener
        )
    }
}