package com.cornershop.counterstest.data.remote

import com.cornershop.counterstest.data.model.counters.Counters
import com.cornershop.counterstest.data.model.inccounter.IdCounter
import com.cornershop.counterstest.domain.api.CounterAPI

class MainDataSource(private val counterAPI: CounterAPI) {

    suspend fun getCounters(): Counters = counterAPI.getCounters()

    suspend fun incCounter(idCounter: IdCounter): Counters = counterAPI.incCounter(idCounter)

    suspend fun decCounter(idCounter: IdCounter): Counters = counterAPI.decCounter(idCounter)
}