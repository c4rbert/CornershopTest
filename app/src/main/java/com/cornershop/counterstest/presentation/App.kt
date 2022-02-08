package com.cornershop.counterstest.presentation

import android.app.Application
import com.cornershop.counterstest.domain.networkconnectivity.NetworkConnectivityChecker

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initNetworkConnectivityChecker()
    }

    private fun initNetworkConnectivityChecker() {
        NetworkConnectivityChecker.init(this.applicationContext)
    }
}