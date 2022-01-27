package com.cornershop.counterstest.repository.main

import com.cornershop.counterstest.data.model.counters.Counters
import com.cornershop.counterstest.data.remote.MainDataSource
import com.cornershop.counterstest.presentation.main.MainActivity

class MainRepository(
    private val context: MainActivity,
    private val mainDataSource: MainDataSource,
) {

    suspend fun getCounters(): Counters = mainDataSource.getCounters()
}