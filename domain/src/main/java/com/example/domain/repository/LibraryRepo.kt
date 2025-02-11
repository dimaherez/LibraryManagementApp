package com.example.domain.repository

import com.example.domain.models.Book
import com.example.domain.models.Review

interface LibraryRepo {

    suspend fun fetchBooks(): List<Book>
    fun borrowBook(id: Int)
    fun returnBook(id: Int)
    fun updateBook(book: Book)
    fun setFavoriteBook(id: Int)
    fun fetchBookById(id: Int): Book?
    suspend fun fetchFavoriteBooks(): List<Book>
    suspend fun fetchTrendingBooks(): List<Book>
    suspend fun fetchTrendingAuthors(): List<String>
    suspend fun fetchTrendingGenres(): List<String>
    suspend fun fetchBooksRecommendation(): List<Book>
    fun postReview(bookId: Int, review: Review)
}