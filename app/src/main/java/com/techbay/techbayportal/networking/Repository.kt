package com.techbay.techbayportal.networking

object Repository {

    const val deviceId = 123
    const val lang = "en"

    private val endpoint = ApiClient.client

    fun getUserAddress(listener: ApiListener<GetUserAddressResponse>) {
        val call = endpoint.getUserAddress(deviceId, lang)
        ApiClient.executeApi(call, listener)
    }

    fun addUserAddress(body: AddUserAddressRequest, listener: ApiListener<AddUserAddressResponse>) {
        val call = endpoint.addUserAddress(body)
        ApiClient.executeApi(call, listener)
    }
}