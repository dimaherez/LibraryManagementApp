package com.example.domain.use_cases

import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class FetchTrendingAuthorsUC @Inject constructor(private val libraryRepo: LibraryRepo) {
    suspend fun fetchTrendingAuthors() = libraryRepo.fetchTrendingAuthors()
}