package com.example.librarymanagementapp.home.favorite_books

import com.example.librarymanagementapp.home.HomeBaseIntent

sealed interface FavoriteBooksIntent: HomeBaseIntent {
    data object FetchData : FavoriteBooksIntent
}