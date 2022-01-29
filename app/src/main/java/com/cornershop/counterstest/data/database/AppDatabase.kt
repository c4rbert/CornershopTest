package com.cornershop.counterstest.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cornershop.counterstest.data.database.daos.CountersDao
import com.cornershop.counterstest.data.database.entities.CountersItemEntity


@Database(entities = [CountersItemEntity::class],
    version = 1,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun counterDao(): CountersDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return instance?: synchronized(this){
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "Counters").allowMainThreadQueries().build()
        }
    }
}