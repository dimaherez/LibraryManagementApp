package com.example.domain.use_cases

import com.example.domain.di.DiReplacer

class ReturnBookUseCase {
    fun returnBook(id: Int) = DiReplacer.libraryRepo.returnBook(id)
}