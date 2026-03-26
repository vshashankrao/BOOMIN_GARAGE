package com.boomingarage.android.ui.customer.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.Bay
import com.boomingarage.shared.domain.model.Garage
import com.boomingarage.shared.domain.model.TimeSlot
import com.boomingarage.shared.domain.usecase.booking.CreateBookingUseCase
import com.boomingarage.shared.domain.usecase.garage.BayAvailability
import com.boomingarage.shared.domain.usecase.garage.GetAvailableBaysUseCase
import com.boomingarage.shared.domain.usecase.garage.GetGarageDetailsUseCase
import com.boomingarage.shared.util.DateTimeUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookingUiState(
    val garage: Garage? = null,
    val bayAvailabilities: List<BayAvailability> = emptyList(),
    val selectedBay: Bay? = null,
    val selectedDate: String = DateTimeUtil.today().toString(),
    val selectedStartTime: String? = null,
    val selectedHours: Int = 1,
    val availableTimeSlots: List<TimeSlot> = emptyList(),
    val isLoading: Boolean = false,
    val isBooking: Boolean = false,
    val error: String? = null,
    val bookingId: String? = null
)

class BookingViewModel(
    private val garageId: String,
    private val getAvailableBays: GetAvailableBaysUseCase,
    private val createBooking: CreateBookingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    init {
        loadAvailability()
    }

    fun loadAvailability() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getAvailableBays(garageId, _uiState.value.selectedDate)
                .onSuccess { bays ->
                    _uiState.value = _uiState.value.copy(
                        bayAvailabilities = bays,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun selectDate(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date, selectedBay = null, selectedStartTime = null)
        loadAvailability()
    }

    fun selectBay(bay: Bay) {
        val slots = _uiState.value.bayAvailabilities.find { it.bay.id == bay.id }?.timeSlots ?: emptyList()
        _uiState.value = _uiState.value.copy(selectedBay = bay, availableTimeSlots = slots, selectedStartTime = null)
    }

    fun selectStartTime(time: String) {
        _uiState.value = _uiState.value.copy(selectedStartTime = time)
    }

    fun selectHours(hours: Int) {
        _uiState.value = _uiState.value.copy(selectedHours = hours)
    }

    fun confirmBooking() {
        val state = _uiState.value
        val bay = state.selectedBay ?: return
        val startTime = state.selectedStartTime ?: return

        val startMinutes = startTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
        val endMinutes = startMinutes + (state.selectedHours * 60)
        val endTime = "${(endMinutes / 60).toString().padStart(2, '0')}:${(endMinutes % 60).toString().padStart(2, '0')}"

        viewModelScope.launch {
            _uiState.value = state.copy(isBooking = true)
            createBooking(
                garageId = garageId,
                garageName = state.garage?.name ?: "",
                bayId = bay.id,
                bayName = bay.name,
                date = state.selectedDate,
                startTime = startTime,
                endTime = endTime,
                hoursBooked = state.selectedHours,
                hourlyRate = bay.hourlyRate ?: state.garage?.hourlyRate ?: 0.0
            ).onSuccess { booking ->
                _uiState.value = _uiState.value.copy(isBooking = false, bookingId = booking.id)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isBooking = false, error = e.message)
            }
        }
    }
}
