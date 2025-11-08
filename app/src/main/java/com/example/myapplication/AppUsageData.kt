package com.example.myapplication
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppUsageData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var app_name: String,
    var package_name: String,
    var time_usage: Long = 0,
    var limit_time: Long = -1,
)