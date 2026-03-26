package com.boomingarage.android.ui.operator.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.Garage
import com.boomingarage.shared.domain.model.UtilizationStats
import com.boomingarage.shared.domain.repository.GarageRepository
import com.boomingarage.shared.domain.usecase.auth.GetCurrentUserUseCase
import com.boomingarage.shared.domain.usecase.stats.GetDashboardStatsUseCase
import com.boomingarage.shared.util.DateTimeUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val garage: Garage? = null,
    val todayStats: UtilizationStats? = null,
    val weeklyStats: UtilizationStats? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class OperatorDashboardViewModel(
    private val getDashboardStats: GetDashboardStatsUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val garageRepository: GarageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val user = getCurrentUser.getOnce() ?: return@launch

            garageRepository.getGaragesByOperator(user.id)
                .onSuccess { garages ->
                    val garage = garages.firstOrNull() ?: return@onSuccess
                    _uiState.value = _uiState.value.copy(garage = garage)

                    val today = DateTimeUtil.today().toString()
                    getDashboardStats.getDaily(garage.id, today)
                        .onSuccess { stats ->
                            _uiState.value = _uiState.value.copy(todayStats = stats)
                        }

                    getDashboardStats.getWeekly(garage.id, today)
                        .onSuccess { stats ->
                            _uiState.value = _uiState.value.copy(weeklyStats = stats)
                        }
                }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}
