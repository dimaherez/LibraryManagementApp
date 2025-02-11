package com.example.data

import com.example.domain.enums.string
import com.example.domain.models.Book
import com.example.domain.models.Review
import com.example.domain.repository.LibraryRepo
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import javax.inject.Inject

class LibraryRepoImpl @Inject constructor() : LibraryRepo {

    override suspend fun fetchBooks(): List<Book> {
        delay(3000)
        return BooksDB.books
    }

    override fun borrowBook(id: Int) {
        val indexById = BooksDB.books.indexOfFirst { it.id == id }
        if (BooksDB.books[indexById].availableCount > 0)
            BooksDB.books[indexById].availableCount -= 1
    }

    override fun returnBook(id: Int) {
        val indexById = BooksDB.books.indexOfFirst { it.id == id }
        BooksDB.books[indexById].availableCount += 1
    }

    override fun updateBook(book: Book) {
        val indexById = BooksDB.books.indexOfFirst { it.id == book.id }
        if (indexById != -1) {
            BooksDB.books[indexById] = book.copy()
        }
    }

    override fun setFavoriteBook(id: Int) {
        val ix = BooksDB.books.indexOfFirst { it.id == id }
        BooksDB.books[ix].isFavorite = BooksDB.books[ix].isFavorite.not()
    }

    override fun fetchBookById(id: Int): Book? = BooksDB.books.find { it.id == id }


    override suspend fun fetchFavoriteBooks(): List<Book> {
        delay(1000)
        return BooksDB.books.filter { it.isFavorite }
    }

    override suspend fun fetchTrendingBooks(): List<Book> {
        delay(1000)
        return BooksDB.bookOrders
            .asSequence()
            .filter { it.orderDateTime.isAfter(LocalDateTime.now().minusMinutes(5)) }
            .groupBy { it.book }
            .map { it.key to it.value.size }
            .sortedByDescending { it.second }
            .map { it.first }
            .toList()
    }

    override suspend fun fetchTrendingAuthors(): List<String> {
        delay(1000)
        return BooksDB.bookOrders
            .asSequence()
            .filter { it.orderDateTime.isAfter(LocalDateTime.now().minusMinutes(5)) }
            .groupBy { it.book.author }
            .map { it.key to it.value.size }
            .sortedByDescending { it.second }
            .map { it.first }
            .toList()
    }

    override suspend fun fetchTrendingGenres(): List<String> {
        delay(1000)
        return BooksDB.bookOrders
            .asSequence()
            .filter { it.orderDateTime.isAfter(LocalDateTime.now().minusMinutes(5)) }
            .groupBy { it.book.genre }
            .map { it.key to it.value.size }
            .sortedByDescending { it.second }
            .map { it.first.string() }
            .toList()
    }

    override suspend fun fetchBooksRecommendation(): List<Book> {
        val favoriteAuthors = BooksDB.books
            .filter { it.isFavorite }
            .map { it.author }

        val favoriteGenres = BooksDB.books
            .filter { it.isFavorite }
            .map { it.genre }

        return BooksDB.books.filter {
                it.author in favoriteAuthors || it.genre in favoriteGenres
            }
    }

    override fun postReview(bookId: Int, review: Review) {
        val indexById = BooksDB.books.indexOfFirst { it.id == bookId }
        val book = BooksDB.books[indexById]
        BooksDB.books[indexById] = book.copy(
            reviews = (book.reviews + review)
        )
    }

}