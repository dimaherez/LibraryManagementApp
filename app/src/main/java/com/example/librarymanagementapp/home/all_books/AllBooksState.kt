package com.example.librarymanagementapp.home.all_books

import com.example.librarymanagementapp.mvi.BaseUiState

sealed class AllBooksState: BaseUiState() {
    data class SectionsBooks(val rvBooksList: List<ListItem>) : AllBooksState()
}