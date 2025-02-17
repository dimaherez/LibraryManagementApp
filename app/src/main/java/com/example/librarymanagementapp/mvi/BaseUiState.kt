package com.example.librarymanagementapp.mvi

open class BaseUiState {
    data object Loading : BaseUiState()
    data class Error(val message: String) : BaseUiState()
}