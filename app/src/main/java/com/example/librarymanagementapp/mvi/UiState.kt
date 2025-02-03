package com.example.librarymanagementapp.mvi

import com.example.librarymanagementapp.models.Book

sealed class UiState {
    data class Loading(val status: LoadingStatus) : UiState()
    data class Data(val data: List<Book>) : UiState()
    data class Error(val message: String) : UiState()
}

enum class LoadingStatus {
    AFTER_ERROR,
    AFTER_SUCCESS,
    INITIAL
}