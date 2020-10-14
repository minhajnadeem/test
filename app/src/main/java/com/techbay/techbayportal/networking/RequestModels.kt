package com.techbay.techbayportal.networking

import com.google.gson.annotations.SerializedName

data class AddUserAddressRequest(
    val lat: Double,
    val long: Double,
    @SerializedName("building_name") val buildingName: String,
    val apartment: String,
    @SerializedName("street_address") val streetAddress: String,
    @SerializedName("area_id") val areaId: Int,
    @SerializedName("city_id") val cityId: Int,
    @SerializedName("device_id") val deviceId: Int,
    @SerializedName("lang") val lang: String
)