package com.example.domain.repository

import com.example.domain.models.Book
import com.example.domain.models.Review

interface LibraryRepo {

    suspend fun fetchBooks(): List<Book>?
    suspend fun borrowBook(id: Int)
    suspend fun returnBook(id: Int)
    suspend fun updateBook(book: Book)
    suspend fun setFavoriteBook(id: Int)
    suspend fun fetchBookById(id: Int): Book?
    suspend fun fetchFavoriteBooks(): List<Book>
    suspend fun fetchTrendingBooks(): List<Book>
    suspend fun fetchTrendingAuthors(): List<String>
    suspend fun fetchTrendingGenres(): List<String>
    suspend fun fetchBooksRecommendation(): List<Book>
    suspend fun postReview(bookId: Int, review: Review)
    suspend fun fetchBooksGroupedByInitial(): Map<Char, List<Book>>?
}