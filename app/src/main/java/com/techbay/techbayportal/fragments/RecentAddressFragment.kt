package com.techbay.techbayportal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.techbay.techbayportal.R
import java.lang.StringBuilder

class RecentAddressFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvAddress = view.findViewById<TextView>(R.id.tvAddress)
        arguments?.apply {
            val fromBundle = RecentAddressFragmentArgs.fromBundle(this)
            val addressData = fromBundle.addressData
            val stringBuilder = StringBuilder()
            stringBuilder.append(addressData.buildingName)
            stringBuilder.appendln(addressData.apartment)
            stringBuilder.appendln(addressData.streetAddress)
            stringBuilder.appendln(addressData.cityId)
            stringBuilder.appendln(addressData.areaId)
            stringBuilder.appendln(addressData.deviceId)
            stringBuilder.appendln(addressData.lat)
            stringBuilder.appendln(addressData.long)
            tvAddress.text = stringBuilder
        }
    }
}