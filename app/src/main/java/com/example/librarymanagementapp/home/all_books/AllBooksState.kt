package com.example.librarymanagementapp.home.all_books

import com.example.domain.models.Book
import com.example.librarymanagementapp.mvi.BaseUiState

sealed class AllBooksState: BaseUiState() {
    data class Books(val books: List<Book>) : AllBooksState()
}