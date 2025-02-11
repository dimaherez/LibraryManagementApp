package com.example.librarymanagementapp.home.trending

import com.example.librarymanagementapp.home.HomeBaseIntent

sealed interface TrendingBooksIntent: HomeBaseIntent {
    data object FetchTrends : TrendingBooksIntent
}