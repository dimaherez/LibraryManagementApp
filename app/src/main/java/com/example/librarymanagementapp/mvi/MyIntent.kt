package com.example.librarymanagementapp.mvi


sealed interface MyIntent {
    data object FetchData : MyIntent
    data object BorrowBook : MyIntent
    data object ReturnBook : MyIntent
}