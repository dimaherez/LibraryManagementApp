package com.example.librarymanagementapp.mvp

import com.example.librarymanagementapp.models.Book

interface ShowBookView : BaseView {

    fun showBooks(books: List<Book>)

    fun showError(message: String)

}