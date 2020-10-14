package com.techbay.techbayportal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techbay.techbayportal.R
import com.techbay.techbayportal.utils.eventbus.GetUserAddressEvent
import com.techbay.techbayportal.utils.eventbus.TechEventBus
import com.techbay.techbayportal.viewmodels.SplashFragmentViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SplashFragment : Fragment(), TechEventBus {

    private lateinit var viewModel: SplashFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onStart() {
        super.onStart()
        register(this)
        viewModel.start()
    }

    override fun onStop() {
        super.onStop()
        unregister(this)
        viewModel.stop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: GetUserAddressEvent) {
        val action = SplashFragmentDirections.actionSplashFragmentToMapFragment()
        findNavController().navigate(action)
    }
}