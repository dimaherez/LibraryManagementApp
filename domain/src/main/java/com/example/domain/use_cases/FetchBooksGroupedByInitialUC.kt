package com.example.domain.use_cases

import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class FetchBooksGroupedByInitialUC @Inject constructor(private val libraryRepo: LibraryRepo) {
    suspend fun fetchBooksGroupedByInitial() = libraryRepo.fetchBooksGroupedByInitial()
}