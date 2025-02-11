package com.example.domain.use_cases

import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class FetchTrendingGenresUC @Inject constructor(private val libraryRepo: LibraryRepo) {
    suspend fun fetchTrendingGenres() = libraryRepo.fetchTrendingGenres()
}