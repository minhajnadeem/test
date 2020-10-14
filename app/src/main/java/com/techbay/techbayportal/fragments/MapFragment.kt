package com.techbay.techbayportal.fragments

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.techbay.techbayportal.R
import com.techbay.techbayportal.base.BaseLocations
import com.techbay.techbayportal.databinding.FragmentMapBinding
import com.techbay.techbayportal.utils.eventbus.MapLocationUpdateEvent
import com.techbay.techbayportal.utils.eventbus.MapNavigateToAddressEvent
import com.techbay.techbayportal.utils.eventbus.TechEventBus
import com.techbay.techbayportal.viewmodels.MapFragmentViewModel
import kotlinx.coroutines.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

class MapFragment : Fragment(), OnMapReadyCallback, TechEventBus {

    private lateinit var binding: FragmentMapBinding
    private lateinit var viewModel: MapFragmentViewModel
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddAddress.isEnabled = false
        binding.btnAddAddress.setOnClickListener {
            viewModel.addAddressClicked()
        }
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        binding.btnCurrent.setOnClickListener {
            viewModel.currentLocationClicked()
        }
    }

    override fun onStart() {
        super.onStart()
        register(this)
        binding.mapView.onStart()
        viewModel.start()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        unregister(this)
        viewModel.stop()
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.apply {
            googleMap = this
            viewModel.onMapReady()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MapLocationUpdateEvent) {
        googleMap?.apply {
            clear()
            addMarker(MarkerOptions().position(event.latLng))
            animateCamera(CameraUpdateFactory.newLatLngZoom(event.latLng, BaseLocations.MAP_ZOOM))
            reverseGeocode(event.latLng)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MapNavigateToAddressEvent) {
        val action = MapFragmentDirections.actionMapFragmentToAddAddressFragment(event.latLng)
        findNavController().navigate(action)
    }

    private fun reverseGeocode(latLng: LatLng) {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(requireActivity())
            val fromLocation = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            val bestMatch: Address? = if (fromLocation.isEmpty()) null else fromLocation.get(0)
            withContext(Dispatchers.Main) {
                binding.btnAddAddress.isEnabled = true
            }
        }
    }
}