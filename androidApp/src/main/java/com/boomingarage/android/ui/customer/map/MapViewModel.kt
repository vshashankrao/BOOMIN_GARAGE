package com.boomingarage.android.ui.customer.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.Garage
import com.boomingarage.shared.domain.usecase.garage.SearchGaragesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MapUiState(
    val garages: List<Garage> = emptyList(),
    val isLoading: Boolean = false,
    val selectedGarage: Garage? = null
)

class MapViewModel(
    private val searchGarages: SearchGaragesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadGarages()
    }

    fun loadGarages(lat: Double = 37.7749, lng: Double = -122.4194) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            searchGarages(lat, lng)
                .onSuccess { garages ->
                    _uiState.value = _uiState.value.copy(garages = garages, isLoading = false)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
        }
    }

    fun selectGarage(garage: Garage?) {
        _uiState.value = _uiState.value.copy(selectedGarage = garage)
    }
}
