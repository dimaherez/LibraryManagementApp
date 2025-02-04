package com.example.librarymanagementapp.mvp

import com.example.domain.models.Book

interface BooksView : BaseView {

    fun showBooks(books: List<com.example.domain.models.Book>)

    fun showError(message: String)

}