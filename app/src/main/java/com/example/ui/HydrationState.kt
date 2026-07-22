package com.example.ui

import com.example.data.WaterLogEntity

data class HydrationState(
    val currentIntakeMl: Int = 0,
    val targetGoalMl: Int = 2000, // 2 Liters default objective
    val todayLogs: List<WaterLogEntity> = emptyList(),
    val streakDays: Int = 1,
    val selectedBeverage: String = "Eau",
    val showResetDialog: Boolean = false,
    val showGoalDialog: Boolean = false,
    val showCustomAmountDialog: Boolean = false,
    val isLoading: Boolean = false
) {
    val progress: Float
        get() = if (targetGoalMl > 0) (currentIntakeMl.toFloat() / targetGoalMl.toFloat()).coerceIn(0f, 1f) else 0f

    val percentage: Int
        get() = (progress * 100).toInt()

    val remainingMl: Int
        get() = (targetGoalMl - currentIntakeMl).coerceAtLeast(0)

    val isGoalReached: Boolean
        get() = currentIntakeMl >= targetGoalMl
}
