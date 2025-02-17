package com.example.domain.use_cases

import com.example.domain.models.Review
import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class PostReviewUC @Inject constructor(private val libraryRepo: LibraryRepo) {
    suspend fun postReview(bookId: Int, review: Review) = libraryRepo.postReview(bookId, review)
}