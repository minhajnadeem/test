package com.techbay.techbayportal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techbay.techbayportal.R
import com.techbay.techbayportal.adapters.SpinnerAreasAdapter
import com.techbay.techbayportal.adapters.SpinnerCitiesAdapter
import com.techbay.techbayportal.databinding.FragmentAddAddressBinding
import com.techbay.techbayportal.utils.eventbus.AddNavigateToRecentAddress
import com.techbay.techbayportal.utils.eventbus.AddSetupAreasEvent
import com.techbay.techbayportal.utils.eventbus.AddSetupCitiesEvent
import com.techbay.techbayportal.utils.eventbus.TechEventBus
import com.techbay.techbayportal.utils.extensions.getString
import com.techbay.techbayportal.viewmodels.AddAddressFragmentViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AddAddressFragment : Fragment(), TechEventBus {

    private lateinit var viewModel: AddAddressFragmentViewModel
    private lateinit var binding: FragmentAddAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddAddressFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvTitle = view.findViewById<TextView>(R.id.tvToolbarTitle)
        tvTitle.text = getString(R.string.str_address_title)
        binding.spinnerCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.spinnerCitiesSelected(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        binding.spinnerAreas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.spinnerAreasSelected(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.btnAdd.isEnabled = false
        binding.btnAdd.setOnClickListener {
            viewModel.btnAddClicked(
                binding.etBuilding.getString(),
                binding.etApt.getString(),
                binding.etStreet.getString(),
                binding.etCity.getString(),
                binding.etArea.getString(),
                binding.etDevice.getString(),
                binding.etLang.getString(),
                binding.etLat.getString(),
                binding.etLng.getString()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        register(this)
        arguments?.apply {
            binding.btnAdd.isEnabled = true
            val fromBundle = AddAddressFragmentArgs.fromBundle(this)
            viewModel.start(fromBundle.currLatLng)
        }
    }

    override fun onResume() {
        super.onResume()
        initObservers()
    }

    override fun onStop() {
        super.onStop()
        unregister(this)
        viewModel.stop()
    }

    private fun initObservers() {
        viewModel.selectedCityId.observe(viewLifecycleOwner, Observer {
            binding.etCity.setText(it.toString())
        })
        viewModel.selectedAreaId.observe(viewLifecycleOwner, Observer {
            binding.etArea.setText(it.toString())

        })
        viewModel.latitude.observe(viewLifecycleOwner, Observer {
            binding.etLat.setText(it.toString())

        })
        viewModel.longitude.observe(viewLifecycleOwner, Observer {
            binding.etLng.setText(it.toString())
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: AddSetupCitiesEvent) {
        binding.spinnerCities.adapter = SpinnerCitiesAdapter(requireContext(), event.cities)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: AddSetupAreasEvent) {
        binding.spinnerAreas.adapter = SpinnerAreasAdapter(requireContext(), event.areasCities)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: AddNavigateToRecentAddress) {
        val action =
            AddAddressFragmentDirections.actionAddAddressFragmentToRecentAddressFragment(event.addressData)
        findNavController().navigate(action)
    }
}