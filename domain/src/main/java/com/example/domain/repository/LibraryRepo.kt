package com.example.domain.repository

import com.example.domain.models.Book

interface LibraryRepo {

    suspend fun fetchBooks(): List<Book>
    fun borrowBook(id: Int)
    fun returnBook(id: Int)
    fun updateBook(id: Int)
    fun setFavoriteBook(id: Int)
}