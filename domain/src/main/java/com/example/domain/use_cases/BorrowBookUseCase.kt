package com.example.domain.use_cases

import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class BorrowBookUseCase @Inject constructor(private val libraryRepo: LibraryRepo) {
    fun borrowBook(id: Int) = libraryRepo.borrowBook(id)
}