package com.cornershop.counterstest.data.remote

import com.cornershop.counterstest.data.model.counters.Counters
import com.cornershop.counterstest.data.model.insertcounter.CreateCounter
import com.cornershop.counterstest.domain.api.CounterAPI

class CreateItemDataSource(private val counterAPI: CounterAPI) {

    suspend fun createCounter(
        createCounter: CreateCounter,
    ): Counters = counterAPI.createCounter(createCounter)
}