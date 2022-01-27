package com.cornershop.counterstest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cornershop.counterstest.core.Resource
import com.cornershop.counterstest.repository.main.MainRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    fun getCounters() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.getCounters()))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}