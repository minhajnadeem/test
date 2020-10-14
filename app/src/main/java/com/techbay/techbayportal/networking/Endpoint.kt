package com.techbay.techbayportal.networking

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Endpoint {

    companion object {
        const val PUBLIC_API_PROD = "public/api/production"
        const val API_V = "/v2"

        const val QUERY_DEVICE_ID = "device_id"
        const val QUERY_LANG = "lang"

        const val GET_USER_ADDRESS = "$PUBLIC_API_PROD$API_V/get-user-addresses"
        const val ADD_USER_ADDRESS = "$PUBLIC_API_PROD$API_V/add-user-address"
    }

    @GET(GET_USER_ADDRESS)
    fun getUserAddress(
        @Query(QUERY_DEVICE_ID) deviceId: Int,
        @Query(QUERY_LANG) lang: String
    ): Call<GetUserAddressResponse>

    @POST(ADD_USER_ADDRESS)
    fun addUserAddress(
        @Body body: AddUserAddressRequest
    ): Call<AddUserAddressResponse>
}