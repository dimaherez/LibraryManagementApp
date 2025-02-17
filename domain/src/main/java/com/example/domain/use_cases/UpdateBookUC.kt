package com.example.domain.use_cases

import com.example.domain.models.Book
import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class UpdateBookUC @Inject constructor(private val libraryRepo: LibraryRepo) {
    suspend fun updateBook(book: Book) = libraryRepo.updateBook(book)
}