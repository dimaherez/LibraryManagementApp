package com.example.domain.use_cases

import com.example.domain.di.DiReplacer

class FetchBooksUseCase {

    suspend fun fetchBooks() = DiReplacer.libraryRepo.fetchBooks()
}