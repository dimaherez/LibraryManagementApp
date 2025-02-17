package com.example.librarymanagementapp.reviews

import com.example.domain.models.Book
import com.example.librarymanagementapp.mvi.BaseUiState

sealed class ReviewsState: BaseUiState() {
    data class BookData(val book: Book): ReviewsState()
}