package com.cornershop.counterstest.domain.api

import com.cornershop.counterstest.data.model.counters.Counters
import com.cornershop.counterstest.data.model.inccounter.IdCounter
import com.cornershop.counterstest.data.model.insertcounter.CreateCounter
import okhttp3.internal.http.hasBody
import retrofit2.http.*

interface CounterAPI {

    @GET("/api/v1/counters")
    suspend fun getCounters(
    ): Counters

    @POST("/api/v1/counter")
    suspend fun createCounter(
        @Body createCounter: CreateCounter,
    ): Counters

    @POST("/api/v1/counter/inc")
    suspend fun incCounter(
        @Body idCounter: IdCounter,
    ): Counters

    @POST("/api/v1/counter/dec")
    suspend fun decCounter(
        @Body idCounter: IdCounter,
    ): Counters


    @HTTP(method = "DELETE", path ="/api/v1/counter", hasBody = true)
    suspend fun deleteCounter(
        @Body idCounter: IdCounter,
    ): Counters
}