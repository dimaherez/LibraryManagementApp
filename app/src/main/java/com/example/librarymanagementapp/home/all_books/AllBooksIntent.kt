package com.example.librarymanagementapp.home.all_books

import com.example.librarymanagementapp.home.HomeBaseIntent


sealed interface AllBooksIntent: HomeBaseIntent {
    data object FetchAllBooks : AllBooksIntent
}