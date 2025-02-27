package com.example.librarymanagementapp.home.all_books.all_books_adapter

import com.example.domain.models.Book

sealed class ListItem {
    data class Section(val letter: Char) : ListItem()
    data class BookInfo(val book: Book) : ListItem()
}