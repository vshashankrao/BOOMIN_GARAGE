package com.boomingarage.android.ui.customer.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.Garage
import com.boomingarage.shared.domain.usecase.auth.GetCurrentUserUseCase
import com.boomingarage.shared.domain.usecase.garage.SearchGaragesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val garages: List<Garage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val userName: String = ""
)

class HomeViewModel(
    private val searchGarages: SearchGaragesUseCase,
    private val getCurrentUser: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUser()
        loadGarages()
    }

    private fun loadUser() {
        viewModelScope.launch {
            val user = getCurrentUser.getOnce()
            _uiState.value = _uiState.value.copy(userName = user?.displayName ?: "")
        }
    }

    fun loadGarages(lat: Double = 37.7749, lng: Double = -122.4194) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            searchGarages(lat, lng)
                .onSuccess { garages ->
                    val filtered = if (_uiState.value.searchQuery.isNotBlank()) {
                        garages.filter {
                            it.name.contains(_uiState.value.searchQuery, ignoreCase = true) ||
                            it.address.contains(_uiState.value.searchQuery, ignoreCase = true)
                        }
                    } else garages
                    _uiState.value = _uiState.value.copy(garages = filtered, isLoading = false, error = null)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        loadGarages()
    }
}
