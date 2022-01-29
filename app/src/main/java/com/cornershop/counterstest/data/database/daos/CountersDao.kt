package com.cornershop.counterstest.data.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cornershop.counterstest.data.database.entities.CountersItemEntity
import com.cornershop.counterstest.data.model.counters.Counters

@Dao
interface CountersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCounters(counterItem: Counters)

    @Query("DELETE FROM Counters")
    fun deleteCounters()

}