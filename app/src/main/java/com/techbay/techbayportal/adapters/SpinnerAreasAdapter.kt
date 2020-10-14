package com.techbay.techbayportal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.techbay.techbayportal.R
import com.techbay.techbayportal.networking.AreasCities
import com.techbay.techbayportal.networking.Cities

class SpinnerAreasAdapter(val ctx: Context, val list: List<AreasCities>) :
    ArrayAdapter<AreasCities>(ctx, android.R.layout.simple_spinner_item, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position)
    }

    private fun getCustomView(position: Int): View {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.spinner_item_simple, null, false)
        val area = list[position]
        val tvCity = view.findViewById<TextView>(R.id.tvCity)
        tvCity.text = area.name
        return view
    }
}