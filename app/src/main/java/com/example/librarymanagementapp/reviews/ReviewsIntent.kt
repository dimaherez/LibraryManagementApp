package com.example.librarymanagementapp.reviews

import com.example.domain.models.Book
import com.example.domain.models.Review

sealed interface ReviewsIntent {
    data class FetchBookById(val id: Int): ReviewsIntent
    data class PostReview(val bookId: Int, val review: Review): ReviewsIntent
}