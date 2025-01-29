package com.example.librarymanagementapp

import com.example.librarymanagementapp.models.Book

sealed class UiState {
    data object Loading : UiState()
    data class Data(val data: List<Book>) : UiState()
    data class Error(val message: String) : UiState()
}