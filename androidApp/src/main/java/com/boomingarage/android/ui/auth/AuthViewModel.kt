package com.boomingarage.android.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.CarInfo
import com.boomingarage.shared.domain.model.User
import com.boomingarage.shared.domain.model.UserRole
import com.boomingarage.shared.domain.usecase.auth.GetCurrentUserUseCase
import com.boomingarage.shared.domain.usecase.auth.SignInUseCase
import com.boomingarage.shared.domain.usecase.auth.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _uiState.value = _uiState.value.copy(user = user)
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            signInUseCase(email, password)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(user = user, isLoading = false)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun signUp(
        email: String,
        password: String,
        displayName: String,
        phone: String,
        role: UserRole = UserRole.CUSTOMER
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            signUpUseCase(email, password, displayName, phone, role)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(user = user, isLoading = false)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.value = AuthUiState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
