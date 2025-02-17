package com.example.domain.use_cases

import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class FetchBookByIdUC @Inject constructor(private val libraryRepo: LibraryRepo) {
    suspend fun fetchBookById(id: Int) = libraryRepo.fetchBookById(id)
}