package com.example.librarymanagementapp.home.all_books

import com.example.librarymanagementapp.home.HomeBaseIntent


sealed interface AllBooksIntent: HomeBaseIntent {
    data object FetchAllBooks : AllBooksIntent
    data object AddBook : AllBooksIntent
    data class SetFavorite(val id: Int) : AllBooksIntent
    data class ResetFavorite(val id: Int) : AllBooksIntent
}