package com.example.librarymanagementapp.home

interface HomeBaseIntent {
    data class ToggleFavoriteBook(val id: Int) : HomeBaseIntent
}