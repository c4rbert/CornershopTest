package com.cornershop.counterstest.util

import android.content.Context
import com.cornershop.counterstest.data.database.AppDatabase

fun getDbInstance(context: Context): AppDatabase {
    return AppDatabase.getInstance(context)
}