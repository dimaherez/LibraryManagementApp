package com.example.librarymanagementapp.home.trending

import com.example.domain.models.Book
import com.example.librarymanagementapp.mvi.BaseUiState

sealed class TrendingBooksState: BaseUiState() {
    data class Trending(val books: List<Book>, val authors: List<String>, val genres: List<String>): TrendingBooksState()
}