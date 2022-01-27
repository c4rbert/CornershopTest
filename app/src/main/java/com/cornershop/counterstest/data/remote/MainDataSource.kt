package com.cornershop.counterstest.data.remote

import com.cornershop.counterstest.data.model.counters.Counters
import com.cornershop.counterstest.domain.api.CounterAPI

class MainDataSource(private val counterAPI: CounterAPI) {

    suspend fun getCounters(): Counters = counterAPI.getCounters()
}