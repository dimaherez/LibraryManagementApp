package com.example.librarymanagementapp.reviews

import androidx.lifecycle.ViewModel
import com.example.domain.models.Book
import com.example.domain.models.Review
import com.example.domain.use_cases.FetchBookByIdUC
import com.example.domain.use_cases.PostReviewUC
import com.example.domain.use_cases.UpdateBookUC
import com.example.librarymanagementapp.mvi.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val fetchBookByIdUC: FetchBookByIdUC,
    private val postReviewUC: PostReviewUC
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun processIntent(intent: ReviewsIntent) {
        when (intent) {
            is ReviewsIntent.FetchBookById -> fetchBookById(intent.id)
            is ReviewsIntent.PostReview -> postReview(intent.bookId, intent.review)
        }
    }

    private fun postReview(bookId: Int, review: Review) {
        postReviewUC.postReview(bookId, review)
    }

    private fun fetchBookById(id: Int) {
        _uiState.value = UiState.Loading
        _uiState.value = UiState.BookData(fetchBookByIdUC.fetchBookById(id))
    }
}