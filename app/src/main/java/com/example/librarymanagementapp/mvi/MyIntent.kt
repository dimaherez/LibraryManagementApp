package com.example.librarymanagementapp.mvi


sealed interface MyIntent {
    data object FetchData : MyIntent
    data class BorrowBook(val id: Int) : MyIntent
    data class ReturnBook(val id: Int) : MyIntent
}