package com.cornershop.counterstest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cornershop.counterstest.core.Resource
import com.cornershop.counterstest.data.model.insertcounter.CreateCounter
import com.cornershop.counterstest.repository.creatcounter.CreateItemRepository
import kotlinx.coroutines.Dispatchers

class CreateItemViewModel(private val repository: CreateItemRepository) : ViewModel() {

    fun createCounter(createCounter: CreateCounter) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.createCounter(createCounter)))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }

}