package com.techbay.techbayportal.viewmodels

import androidx.lifecycle.ViewModel
import com.techbay.techbayportal.networking.ApiListener
import com.techbay.techbayportal.networking.GetUserAddressResponse
import com.techbay.techbayportal.networking.Repository
import com.techbay.techbayportal.utils.eventbus.GetUserAddressEvent
import com.techbay.techbayportal.utils.eventbus.TechEventBus

class SplashFragmentViewModel : ViewModel(), TechEventBus {


    private val listener = object : ApiListener<GetUserAddressResponse> {
        override fun onSuccess(body: GetUserAddressResponse?) {
            hideLoading()
            body?.apply {
                if (body.status) {
                    handleResponse(this)
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

    fun start() {
        getUserAddresses()
    }

    fun stop() {
    }

    private fun getUserAddresses() {
        showLoading()
        Repository.getUserAddress(listener)
    }

    private fun handleResponse(getUserAddressResponse: GetUserAddressResponse) {
        postStickyEvent(GetUserAddressEvent(getUserAddressResponse))
    }
}