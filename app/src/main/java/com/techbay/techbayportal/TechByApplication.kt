package com.techbay.techbayportal

import android.app.Application
import timber.log.Timber

class TechByApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}