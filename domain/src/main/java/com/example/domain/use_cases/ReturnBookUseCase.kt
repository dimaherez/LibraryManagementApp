package com.example.domain.use_cases

import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class ReturnBookUseCase @Inject constructor(private val libraryRepo: LibraryRepo) {
    suspend fun returnBook(id: Int) = libraryRepo.returnBook(id)
}