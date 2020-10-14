package com.techbay.techbayportal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.techbay.techbayportal.base.BaseLocations
import com.techbay.techbayportal.base.BasePermissions
import com.techbay.techbayportal.databinding.ActivityMainBinding
import com.techbay.techbayportal.utils.eventbus.ShowLoadingEvent
import com.techbay.techbayportal.utils.eventbus.ShowSnackBarEvent
import com.techbay.techbayportal.utils.eventbus.TechEventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity(), TechEventBus {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        lifecycle.addObserver(BasePermissions(this))
        lifecycle.addObserver(BaseLocations(this))
    }

    override fun onStart() {
        register(this)
        super.onStart()
    }

    override fun onStop() {
        unregister(this)
        super.onStop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ShowLoadingEvent) {
        if (event.show) {
            binding.layoutLoading.visibility = View.VISIBLE
        } else {
            binding.layoutLoading.visibility = View.GONE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ShowSnackBarEvent) {
        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
    }
}