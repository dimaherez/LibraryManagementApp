package com.example.domain.use_cases

import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class BooksRecommendationUC @Inject constructor(private val libraryRepo: LibraryRepo) {
    suspend fun fetchBooksRecommendation() = libraryRepo.fetchBooksRecommendation()
}