package com.example.data

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HydrationRepository(private val waterLogDao: WaterLogDao) {

    fun getTodayLogs(dateString: String): Flow<List<WaterLogEntity>> {
        return waterLogDao.getLogsForDate(dateString)
    }

    fun getTodayTotalIntake(dateString: String): Flow<Int?> {
        return waterLogDao.getTotalForDate(dateString)
    }

    fun getAllLoggedDates(): Flow<List<String>> {
        return waterLogDao.getAllLoggedDates()
    }

    suspend fun addWater(amountMl: Int, beverageType: String, dateString: String) {
        val log = WaterLogEntity(
            amountMl = amountMl,
            beverageType = beverageType,
            timestamp = System.currentTimeMillis(),
            dateString = dateString
        )
        waterLogDao.insertLog(log)
    }

    suspend fun deleteLog(log: WaterLogEntity) {
        waterLogDao.deleteLog(log)
    }

    suspend fun resetToday(dateString: String) {
        waterLogDao.clearLogsForDate(dateString)
    }

    companion object {
        fun getCurrentDateString(): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return formatter.format(Date())
        }
    }
}
