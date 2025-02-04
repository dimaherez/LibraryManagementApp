package com.example.domain.use_cases

import com.example.domain.di.DiReplacer

class BorrowBookUseCase {
    fun borrowBook(id: Int) = DiReplacer.libraryRepo.borrowBook(id)
}