package com.example.librarymanagementapp.mvi

import com.example.librarymanagementapp.models.Book


sealed interface MyIntent {
    data object FetchData : MyIntent
    data class BorrowBook(val id: Int) : MyIntent
    data class ReturnBook(val id: Int) : MyIntent
}