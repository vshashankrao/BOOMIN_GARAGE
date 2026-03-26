package com.boomingarage.android.ui.operator.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.Booking
import com.boomingarage.shared.domain.repository.GarageRepository
import com.boomingarage.shared.domain.usecase.auth.GetCurrentUserUseCase
import com.boomingarage.shared.domain.usecase.booking.GetGarageBookingsUseCase
import com.boomingarage.shared.util.DateTimeUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ManageBookingsUiState(
    val bookings: List<Booking> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTab: Int = 0, // 0=Today, 1=Upcoming, 2=Past
    val garageId: String? = null
)

class ManageBookingsViewModel(
    private val getGarageBookings: GetGarageBookingsUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val garageRepository: GarageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageBookingsUiState())
    val uiState: StateFlow<ManageBookingsUiState> = _uiState.asStateFlow()

    init {
        loadBookings()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val user = getCurrentUser.getOnce() ?: return@launch
            garageRepository.getGaragesByOperator(user.id)
                .onSuccess { garages ->
                    val garage = garages.firstOrNull() ?: return@onSuccess
                    _uiState.value = _uiState.value.copy(garageId = garage.id)
                    val today = DateTimeUtil.today().toString()
                    getGarageBookings(garage.id, today).collect { bookings ->
                        _uiState.value = _uiState.value.copy(bookings = bookings, isLoading = false)
                    }
                }
        }
    }

    fun selectTab(tab: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }
}
