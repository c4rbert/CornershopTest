package com.cornershop.counterstest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cornershop.counterstest.core.Resource
import com.cornershop.counterstest.data.database.entities.CountersItemEntity
import com.cornershop.counterstest.data.model.counters.Counters
import com.cornershop.counterstest.data.model.inccounter.IdCounter
import com.cornershop.counterstest.data.repositories.main.MainRepository
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

    fun getSavedCounters() = repository.getSavedCounters()

    fun incCounter(idCounter: IdCounter) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.incCounter(idCounter)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun decCounter(idCounter: IdCounter) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.decCounter(idCounter)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun deleteCounter(idCounter: IdCounter) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.deleteCounter(idCounter)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun insertCounters(counters: ArrayList<CountersItemEntity>) {
        repository.insertCounters(counters)
    }

    fun deleteCounters() {
        repository.deleteCounters()
    }

    fun deleteSelectedCounters(selectedCounters: List<String>) {
        repository.deleteSelectedCounters(selectedCounters)
    }
}