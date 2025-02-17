package com.example.librarymanagementapp.info

sealed interface BookInfoIntent {
    data class FetchBookById(val id: Int): BookInfoIntent
}