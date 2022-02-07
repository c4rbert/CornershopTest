package com.cornershop.counterstest.domain.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.cornershop.counterstest.data.model.inccounter.IdCounter
import com.cornershop.counterstest.domain.api.CounterAPI
import com.cornershop.counterstest.util.getDbInstance
import gentera.yastas.yas_app_client_gestion_ventas.api.client.Client
import kotlinx.coroutines.*
import com.cornershop.counterstest.data.database.entities.CountersItemEntity
import com.cornershop.counterstest.data.model.counters.Counters

import com.google.gson.Gson

class DeleteWorker(val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    private val db = getDbInstance(context)
    private val countersAPI = Client(context).getClient(CounterAPI::class.java) as CounterAPI
    private lateinit var outputData: Data
    override suspend fun doWork(): Result {
        return try {
            val data: Data = inputData
            val counters = deserializeFromJson(data.getString("task"))
            if (counters != null) {
                for (item in counters)
                    deleteCounter(item.id)
                outputData = workDataOf("task" to "task complete")
            }
            Result.success(outputData)
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun deserializeFromJson(jsonString: String?): ArrayList<CountersItemEntity>? {
        val gson = Gson()
        return gson.fromJson(jsonString, Counters::class.java)
    }

    private suspend fun deleteCounter(id: String) {
        CoroutineScope(Dispatchers.IO).runCatching {
            val response = countersAPI.deleteCounter(IdCounter(id))
        }.onFailure {
            outputData = workDataOf("task" to "failure")
        }.onSuccess {
            outputData = workDataOf("task" to "onSuccess")
        }
    }
}