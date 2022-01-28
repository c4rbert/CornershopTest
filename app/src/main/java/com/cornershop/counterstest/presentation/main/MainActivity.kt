package com.cornershop.counterstest.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.cornershop.counterstest.R
import com.cornershop.counterstest.data.remote.MainDataSource
import com.cornershop.counterstest.databinding.ActivityMainBinding
import com.cornershop.counterstest.domain.api.CounterAPI
import com.cornershop.counterstest.presentation.main.adapter.CountersAdapter
import com.cornershop.counterstest.repository.main.MainRepository
import com.cornershop.counterstest.viewmodel.MainViewModel
import gentera.yastas.yas_app_client_gestion_ventas.api.client.Client
import gentera.yastas.yas_app_client_gestion_ventas.viewmodel.factory.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}