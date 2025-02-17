package com.example.librarymanagementapp.edit

import com.example.domain.models.Book
import com.example.librarymanagementapp.mvi.BaseUiState

sealed class BookEditState: BaseUiState() {
    data class BookData(val book: Book): BookEditState()
}