package com.boomingarage.android.ui.customer.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.Booking
import com.boomingarage.shared.domain.usecase.auth.GetCurrentUserUseCase
import com.boomingarage.shared.domain.usecase.booking.GetUserBookingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookingHistoryUiState(
    val upcomingBookings: List<Booking> = emptyList(),
    val pastBookings: List<Booking> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTab: Int = 0
)

class BookingHistoryViewModel(
    private val getUserBookings: GetUserBookingsUseCase,
    private val getCurrentUser: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingHistoryUiState())
    val uiState: StateFlow<BookingHistoryUiState> = _uiState.asStateFlow()

    init {
        loadBookings()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val user = getCurrentUser.getOnce() ?: return@launch
            getUserBookings.getUpcoming(user.id)
                .onSuccess { upcoming ->
                    _uiState.value = _uiState.value.copy(upcomingBookings = upcoming)
                }
            getUserBookings.getPast(user.id)
                .onSuccess { past ->
                    _uiState.value = _uiState.value.copy(pastBookings = past)
                }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun selectTab(tab: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }
}
