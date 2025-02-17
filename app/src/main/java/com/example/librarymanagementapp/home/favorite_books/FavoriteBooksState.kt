package com.example.librarymanagementapp.home.favorite_books

import com.example.domain.models.Book
import com.example.librarymanagementapp.mvi.BaseUiState

sealed class FavoriteBooksState: BaseUiState() {
    data class FavoriteBooks(val favoriteBooks: List<Book>, val recommendations: List<Book>): FavoriteBooksState()
}