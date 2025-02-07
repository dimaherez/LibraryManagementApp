package com.example.librarymanagementapp.books


sealed interface BooksIntent {
    data object FetchData : BooksIntent
    data class SetFavoriteBook(val id: Int) : BooksIntent
}