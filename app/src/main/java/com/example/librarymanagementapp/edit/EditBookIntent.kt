package com.example.librarymanagementapp.edit

import com.example.domain.models.Book

sealed interface EditBookIntent {
    data class FetchBookById(val id: Int): EditBookIntent
    data class UpdateBook(val book: Book): EditBookIntent
}