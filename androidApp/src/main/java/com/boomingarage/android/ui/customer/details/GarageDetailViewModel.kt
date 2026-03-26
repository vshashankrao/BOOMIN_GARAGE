package com.boomingarage.android.ui.customer.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.Bay
import com.boomingarage.shared.domain.model.Garage
import com.boomingarage.shared.domain.model.Review
import com.boomingarage.shared.domain.usecase.garage.GetGarageDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GarageDetailUiState(
    val garage: Garage? = null,
    val bays: List<Bay> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class GarageDetailViewModel(
    private val garageId: String,
    private val getGarageDetails: GetGarageDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GarageDetailUiState())
    val uiState: StateFlow<GarageDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetails()
    }

    fun loadDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getGarageDetails(garageId)
                .onSuccess { details ->
                    _uiState.value = GarageDetailUiState(
                        garage = details.garage,
                        bays = details.bays,
                        reviews = details.reviews,
                        isLoading = false
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }
}
