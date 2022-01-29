package com.cornershop.counterstest.data.repositories.main

import android.content.Context
import com.cornershop.counterstest.data.model.counters.Counters
import com.cornershop.counterstest.data.model.inccounter.IdCounter
import com.cornershop.counterstest.data.remote.MainDataSource
import com.cornershop.counterstest.util.getDbInstance

class MainRepository(
    private val context: Context,
    private val mainDataSource: MainDataSource,
) {

    suspend fun getCounters(): Counters = mainDataSource.getCounters()

    suspend fun incCounter(idCounter: IdCounter): Counters = mainDataSource.incCounter(idCounter)

    suspend fun decCounter(idCounter: IdCounter): Counters = mainDataSource.decCounter(idCounter)

    fun insertCounters(counters: Counters) {
        getDbInstance(context).counterDao().insertCounters(counters)
    }

    fun deleteCounters() {
        getDbInstance(context).counterDao().deleteCounters()
    }
}