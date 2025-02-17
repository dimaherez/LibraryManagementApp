package com.example.librarymanagementapp.reviews

import androidx.lifecycle.ViewModel
import com.example.domain.models.Review
import com.example.domain.use_cases.FetchBookByIdUC
import com.example.domain.use_cases.PostReviewUC
import com.example.librarymanagementapp.mvi.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val fetchBookByIdUC: FetchBookByIdUC,
    private val postReviewUC: PostReviewUC
) : ViewModel() {

    private val _uiState = MutableStateFlow<BaseUiState>(BaseUiState.Loading)
    val uiState: StateFlow<BaseUiState> = _uiState

    fun processIntent(intent: ReviewsIntent) {
        when (intent) {
            is ReviewsIntent.FetchBookById -> fetchBookById(intent.id)
            is ReviewsIntent.PostReview -> postReview(intent.bookId, intent.review)
        }
    }

    private fun postReview(bookId: Int, review: Review) {
        CoroutineScope(Dispatchers.IO).launch {
            postReviewUC.postReview(bookId, review)
        }
    }

    private fun fetchBookById(id: Int) {
        _uiState.value = BaseUiState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val response = fetchBookByIdUC.fetchBookById(id)
            if (response != null)
                _uiState.value = ReviewsState.BookData(response)
            else
                _uiState.value = BaseUiState.Error("Book not found")
        }
    }
}