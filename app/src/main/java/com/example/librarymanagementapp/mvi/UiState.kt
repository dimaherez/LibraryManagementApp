package com.example.librarymanagementapp.mvi

import com.example.domain.models.Book

sealed class UiState {
    data object Loading : UiState()
    data class Books(val books: List<Book>) : UiState()
    data class BookData(val book: Book?): UiState()
    data class BooksRecommendation(val data: List<Book>): UiState()
    data class Trending(val books: List<Book>, val authors: List<String>, val genres: List<String>): UiState()
    data class FavoriteBooks(val favoriteBooks: List<Book>, val recommendations: List<Book>): UiState()
    data class Error(val message: String) : UiState()
}