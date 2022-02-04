package com.cornershop.counterstest.data.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Counters")
data class CountersItemEntity(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id: String,
    val count: Int,
    val title: String,
    var removable: Boolean = false,
)