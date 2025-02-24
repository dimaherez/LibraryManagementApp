package com.example.domain.use_cases

import com.example.domain.models.Book
import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class AddBookUC @Inject constructor(private val libraryRepo: LibraryRepo) {
    suspend fun addBook(book: Book) = libraryRepo.addBook(book)
}