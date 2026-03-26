package com.boomingarage.android.ui.customer.billing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.BillingSummary
import com.boomingarage.shared.domain.usecase.payment.ProcessPaymentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BillingUiState(
    val summary: BillingSummary? = null,
    val isLoading: Boolean = false,
    val isPaying: Boolean = false,
    val isPaid: Boolean = false,
    val error: String? = null
)

class BillingViewModel(
    private val bookingId: String,
    private val processPayment: ProcessPaymentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BillingUiState())
    val uiState: StateFlow<BillingUiState> = _uiState.asStateFlow()

    init {
        loadBilling()
    }

    private fun loadBilling() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            processPayment.getBillingSummary(bookingId)
                .onSuccess { summary ->
                    _uiState.value = _uiState.value.copy(summary = summary, isLoading = false)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun pay() {
        val summary = _uiState.value.summary ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPaying = true)
            processPayment.initiatePayment(bookingId, summary.total)
                .onSuccess { clientSecret ->
                    // In production, this launches Stripe PaymentSheet
                    processPayment.confirmPayment(clientSecret)
                    _uiState.value = _uiState.value.copy(isPaying = false, isPaid = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isPaying = false, error = e.message)
                }
        }
    }
}
