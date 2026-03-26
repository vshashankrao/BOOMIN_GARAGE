package com.boomingarage.android.ui.operator.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.UtilizationStats
import com.boomingarage.shared.domain.repository.GarageRepository
import com.boomingarage.shared.domain.usecase.auth.GetCurrentUserUseCase
import com.boomingarage.shared.domain.usecase.stats.GetDashboardStatsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RevenueStatsUiState(
    val revenueHistory: List<UtilizationStats> = emptyList(),
    val isLoading: Boolean = false,
    val selectedDays: Int = 7
)

class RevenueStatsViewModel(
    private val getDashboardStats: GetDashboardStatsUseCase,
    private val getCurrentUser: GetCurrentUserUseCase
) : ViewModel() {

    private var garageId: String? = null

    private val _uiState = MutableStateFlow(RevenueStatsUiState())
    val uiState: StateFlow<RevenueStatsUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val user = getCurrentUser.getOnce() ?: return@launch

            // For now, use a hardcoded garage ID pattern
            // In production, fetch operator's garage
            getDashboardStats.getRevenueHistory(garageId ?: "", _uiState.value.selectedDays)
                .onSuccess { history ->
                    _uiState.value = _uiState.value.copy(revenueHistory = history, isLoading = false)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
        }
    }

    fun selectDays(days: Int) {
        _uiState.value = _uiState.value.copy(selectedDays = days)
        loadStats()
    }
}
