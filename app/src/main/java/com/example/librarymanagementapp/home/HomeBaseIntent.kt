package com.example.librarymanagementapp.home

interface HomeBaseIntent {
    data class SetFavoriteBook(val id: Int) : HomeBaseIntent
}