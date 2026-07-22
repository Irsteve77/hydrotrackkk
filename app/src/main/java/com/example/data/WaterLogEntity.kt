package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_logs")
data class WaterLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amountMl: Int,
    val beverageType: String = "Eau",
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String // Format YYYY-MM-DD
)
