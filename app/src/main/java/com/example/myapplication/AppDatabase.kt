package com.example.myapplication

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AppUsageData::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appUsageDao(): AppUsageDao


}
