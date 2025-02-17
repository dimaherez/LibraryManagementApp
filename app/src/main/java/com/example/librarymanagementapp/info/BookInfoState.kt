package com.example.librarymanagementapp.info

import com.example.domain.models.Book
import com.example.librarymanagementapp.mvi.BaseUiState

sealed class BookInfoState: BaseUiState() {
    data class BookData(val book: Book): BookInfoState()
}