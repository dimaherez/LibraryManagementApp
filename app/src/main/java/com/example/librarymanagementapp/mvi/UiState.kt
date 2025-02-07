package com.example.librarymanagementapp.mvi

import com.example.domain.models.Book
import kotlinx.coroutines.flow.Flow

sealed class UiState {
    data object Loading : UiState()
    data class Data(val data: List<Book>) : UiState()
    data class Error(val message: String) : UiState()
}

enum class LoadingStatus {
    AFTER_ERROR,
    AFTER_SUCCESS,
    INITIAL
}