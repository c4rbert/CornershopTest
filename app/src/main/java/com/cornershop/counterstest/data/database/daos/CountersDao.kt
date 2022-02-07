package com.cornershop.counterstest.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cornershop.counterstest.data.database.entities.CountersItemEntity

@Dao
interface CountersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCounters(counterItem: ArrayList<CountersItemEntity>)

    @Query("SELECT * FROM Counters")
    fun getSavedCounters(): List<CountersItemEntity>?

    @Query("DELETE FROM Counters")
    fun deleteCounters()

    @Query("DELETE FROM Counters WHERE id in (:selectedCounters)")
    fun deleteSelectedCounters(selectedCounters: List<String>)

}