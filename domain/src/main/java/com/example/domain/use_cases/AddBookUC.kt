package com.example.domain.use_cases

import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class AddBookUC @Inject constructor(private val libraryRepo: LibraryRepo) {
    suspend fun addBook() = libraryRepo.addBook()
}