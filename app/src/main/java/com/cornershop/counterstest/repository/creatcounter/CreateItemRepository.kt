package com.cornershop.counterstest.repository.creatcounter

import com.cornershop.counterstest.data.model.counters.Counters
import com.cornershop.counterstest.data.model.insertcounter.CreateCounter
import com.cornershop.counterstest.data.remote.CreateItemDataSource
import com.cornershop.counterstest.presentation.main.fragments.CreateItemFragment

class CreateItemRepository(
    private val context: CreateItemFragment,
    private val createItemDataSource: CreateItemDataSource,
) {
    suspend fun createCounter(createCounter: CreateCounter): Counters =
        createItemDataSource.createCounter(createCounter)
}