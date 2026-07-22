package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterLogDao {
    @Query("SELECT * FROM water_logs WHERE dateString = :date ORDER BY timestamp DESC")
    fun getLogsForDate(date: String): Flow<List<WaterLogEntity>>

    @Query("SELECT SUM(amountMl) FROM water_logs WHERE dateString = :date")
    fun getTotalForDate(date: String): Flow<Int?>

    @Query("SELECT DISTINCT dateString FROM water_logs ORDER BY dateString DESC")
    fun getAllLoggedDates(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: WaterLogEntity)

    @Delete
    suspend fun deleteLog(log: WaterLogEntity)

    @Query("DELETE FROM water_logs WHERE dateString = :date")
    suspend fun clearLogsForDate(date: String)
}
