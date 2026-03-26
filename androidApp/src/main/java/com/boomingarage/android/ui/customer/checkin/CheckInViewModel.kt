package com.boomingarage.android.ui.customer.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.Booking
import com.boomingarage.shared.domain.model.CheckInSession
import com.boomingarage.shared.domain.repository.BookingRepository
import com.boomingarage.shared.domain.repository.CheckInRepository
import com.boomingarage.shared.domain.usecase.checkin.EndCheckInUseCase
import com.boomingarage.shared.domain.usecase.checkin.StartCheckInUseCase
import com.boomingarage.shared.util.DateTimeUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CheckInUiState(
    val booking: Booking? = null,
    val session: CheckInSession? = null,
    val isCheckedIn: Boolean = false,
    val remainingSeconds: Long = 0,
    val isOvertime: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val sessionEnded: Boolean = false
)

class CheckInViewModel(
    private val bookingId: String,
    private val startCheckIn: StartCheckInUseCase,
    private val endCheckIn: EndCheckInUseCase,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckInUiState())
    val uiState: StateFlow<CheckInUiState> = _uiState.asStateFlow()

    init {
        loadBooking()
    }

    private fun loadBooking() {
        viewModelScope.launch {
            bookingRepository.getById(bookingId)
                .onSuccess { booking ->
                    _uiState.value = _uiState.value.copy(booking = booking)
                }
        }
    }

    fun checkIn() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            startCheckIn(bookingId)
                .onSuccess { session ->
                    _uiState.value = _uiState.value.copy(
                        session = session,
                        isCheckedIn = true,
                        isLoading = false
                    )
                    startTimer(session)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    private fun startTimer(session: CheckInSession) {
        viewModelScope.launch {
            while (_uiState.value.isCheckedIn && !_uiState.value.sessionEnded) {
                val now = DateTimeUtil.toEpochMillis(DateTimeUtil.now())
                val remaining = (session.scheduledEndAt - now) / 1000
                _uiState.value = _uiState.value.copy(
                    remainingSeconds = remaining,
                    isOvertime = remaining < 0
                )
                delay(1000)
            }
        }
    }

    fun endSession() {
        val session = _uiState.value.session ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            endCheckIn(session.id, bookingId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        sessionEnded = true,
                        isCheckedIn = false
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun requestAssistance(message: String) {
        // Would trigger assistance request through the repository
    }
}
