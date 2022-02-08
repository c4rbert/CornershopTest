package com.cornershop.counterstest.presentation.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.cornershop.counterstest.R
import com.cornershop.counterstest.domain.networkconnectivity.NetworkConnectivityChecker
import com.google.android.material.snackbar.Snackbar


open class BaseFragment : Fragment() {
    var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setInternetConnectivityObserver()
    }

    override fun onResume() {
        super.onResume()
        NetworkConnectivityChecker.checkForConnection()
    }

    private fun setInternetConnectivityObserver() {
        NetworkConnectivityChecker.observe(this, liveDataObserver)
    }

    private val liveDataObserver: Observer<Boolean> = Observer { isConnected ->
        if (!isConnected) {
            view?.let {
                snackbar = Snackbar.make(it, R.string.no_internet, Snackbar.LENGTH_LONG)
                snackbar?.show()
            }
        } else {
            snackbar?.dismiss()
        }
    }
}