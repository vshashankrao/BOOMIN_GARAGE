package com.boomingarage.android.ui.operator.bays

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.Bay
import com.boomingarage.shared.domain.repository.GarageRepository
import com.boomingarage.shared.domain.usecase.auth.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BayManagementUiState(
    val bays: List<Bay> = emptyList(),
    val isLoading: Boolean = false,
    val garageId: String? = null
)

class BayManagementViewModel(
    private val garageRepository: GarageRepository,
    private val getCurrentUser: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BayManagementUiState())
    val uiState: StateFlow<BayManagementUiState> = _uiState.asStateFlow()

    init {
        loadBays()
    }

    private fun loadBays() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val user = getCurrentUser.getOnce() ?: return@launch
            garageRepository.getGaragesByOperator(user.id)
                .onSuccess { garages ->
                    val garage = garages.firstOrNull() ?: return@onSuccess
                    _uiState.value = _uiState.value.copy(garageId = garage.id)
                    garageRepository.getBays(garage.id)
                        .onSuccess { bays ->
                            _uiState.value = _uiState.value.copy(bays = bays, isLoading = false)
                        }
                }
        }
    }

    fun toggleBayAvailability(bay: Bay) {
        val garageId = _uiState.value.garageId ?: return
        viewModelScope.launch {
            garageRepository.updateBayAvailability(garageId, bay.id, !bay.isAvailable)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        bays = _uiState.value.bays.map {
                            if (it.id == bay.id) it.copy(isAvailable = !bay.isAvailable) else it
                        }
                    )
                }
        }
    }
}
