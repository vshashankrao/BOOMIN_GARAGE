package com.boomingarage.android.ui.customer.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boomingarage.shared.domain.model.Review
import com.boomingarage.shared.domain.usecase.auth.GetCurrentUserUseCase
import com.boomingarage.shared.domain.usecase.review.SubmitReviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WriteReviewUiState(
    val rating: Int = 0,
    val comment: String = "",
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    val error: String? = null
)

class WriteReviewViewModel(
    private val garageId: String,
    private val bookingId: String,
    private val submitReview: SubmitReviewUseCase,
    private val getCurrentUser: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WriteReviewUiState())
    val uiState: StateFlow<WriteReviewUiState> = _uiState.asStateFlow()

    fun setRating(rating: Int) {
        _uiState.value = _uiState.value.copy(rating = rating)
    }

    fun setComment(comment: String) {
        _uiState.value = _uiState.value.copy(comment = comment)
    }

    fun submit() {
        val state = _uiState.value
        if (state.rating == 0) {
            _uiState.value = state.copy(error = "Please select a rating")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSubmitting = true, error = null)
            val user = getCurrentUser.getOnce()

            val review = Review(
                id = "",
                garageId = garageId,
                userId = user?.id ?: "",
                userName = user?.displayName ?: "Anonymous",
                bookingId = bookingId,
                rating = state.rating,
                comment = state.comment
            )

            submitReview(review)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, isSubmitted = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isSubmitting = false, error = e.message)
                }
        }
    }
}
