package com.techbay.techbayportal.networking

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetUserAddressResponse(
    val status: Boolean,
    val code: Int,
    val msg: List<String>,
    val errors: List<String>,
    val data: UserAddressData
)

data class UserAddressData(
    val cities: List<Cities>,
    @SerializedName("areas_of_cities") val areasOfCities: List<AreasCities>
)

data class Cities(
    @SerializedName("city_id") val cityId: Int,
    val name: String
)

data class AreasCities(
    val name: String,
    @SerializedName("shipping_charges") val shippingCharges: String,
    @SerializedName("city_id") val cityId: Int,
    @SerializedName("area_id") val areaId: Int
)

data class AddUserAddressResponse(
    val status: Boolean,
    val code: Int,
    val msg: List<String>,
    val errors: List<String>,
    val data: AddUserAddressData
) : Serializable

class AddUserAddressData(
    val lat: Double,
    val long: Double,
    @SerializedName("building_name") val buildingName: String,
    val apartment: String,
    @SerializedName("street_address") val streetAddress: String,
    @SerializedName("area_id") val areaId: Int,
    @SerializedName("city_id") val cityId: Int,
    @SerializedName("device_id") val deviceId: Int,
    @SerializedName("lang") val lang: String,
    @SerializedName("address_id") val addressId: Int
) : Serializable
