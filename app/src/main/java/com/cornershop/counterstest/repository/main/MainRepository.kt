package com.cornershop.counterstest.repository.main

import com.cornershop.counterstest.data.model.counters.Counters
import com.cornershop.counterstest.data.model.inccounter.IdCounter
import com.cornershop.counterstest.data.remote.MainDataSource
import com.cornershop.counterstest.presentation.main.fragments.MainFragment

class MainRepository(
    private val context: MainFragment,
    private val mainDataSource: MainDataSource,
) {

    suspend fun getCounters(): Counters = mainDataSource.getCounters()

    suspend fun incCounter(idCounter: IdCounter): Counters = mainDataSource.incCounter(idCounter)

    suspend fun decCounter(idCounter: IdCounter): Counters = mainDataSource.decCounter(idCounter)
}