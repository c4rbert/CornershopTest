package com.cornershop.counterstest.domain.api

import com.cornershop.counterstest.data.model.counters.Counters
import retrofit2.http.GET

interface CounterAPI {

    @GET("/api/v1/counters")
    suspend fun getCounters(
    ): Counters
}