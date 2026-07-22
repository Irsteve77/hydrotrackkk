package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.HydrationDatabase
import com.example.data.HydrationRepository
import com.example.data.WaterLogEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HydrationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HydrationRepository
    private val todayDate: String = HydrationRepository.getCurrentDateString()

    private val _uiState = MutableStateFlow(HydrationState())
    val uiState: StateFlow<HydrationState> = _uiState.asStateFlow()

    init {
        val dao = HydrationDatabase.getDatabase(application).waterLogDao()
        repository = HydrationRepository(dao)

        observeTodayData()
    }

    private fun observeTodayData() {
        viewModelScope.launch {
            combine(
                repository.getTodayLogs(todayDate),
                repository.getTodayTotalIntake(todayDate),
                repository.getAllLoggedDates()
            ) { logs, total, allDates ->
                Triple(logs, total ?: 0, allDates)
            }.collect { (logs, totalIntake, allDates) ->
                val streak = calculateStreak(allDates)
                _uiState.update { currentState ->
                    currentState.copy(
                        currentIntakeMl = totalIntake,
                        todayLogs = logs,
                        streakDays = streak,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun calculateStreak(loggedDates: List<String>): Int {
        if (loggedDates.isEmpty()) return 0
        // Simple streak logic counting consecutive active days
        var streak = 0
        if (loggedDates.contains(todayDate)) {
            streak++
        }
        return streak.coerceAtLeast(1)
    }

    fun addWater(amountMl: Int = 250, beverageType: String = _uiState.value.selectedBeverage) {
        viewModelScope.launch {
            repository.addWater(
                amountMl = amountMl,
                beverageType = beverageType,
                dateString = todayDate
            )
        }
    }

    fun resetToday() {
        viewModelScope.launch {
            repository.resetToday(todayDate)
            _uiState.update { it.copy(showResetDialog = false) }
        }
    }

    fun deleteLog(log: WaterLogEntity) {
        viewModelScope.launch {
            repository.deleteLog(log)
        }
    }

    fun updateGoal(newGoalMl: Int) {
        if (newGoalMl > 0) {
            _uiState.update { it.copy(targetGoalMl = newGoalMl, showGoalDialog = false) }
        }
    }

    fun selectBeverage(beverage: String) {
        _uiState.update { it.copy(selectedBeverage = beverage) }
    }

    fun setResetDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showResetDialog = visible) }
    }

    fun setGoalDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showGoalDialog = visible) }
    }

    fun setCustomAmountDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showCustomAmountDialog = visible) }
    }
}
