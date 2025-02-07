package com.example.data

import com.example.domain.models.Book
import com.example.domain.repository.LibraryRepo
import kotlinx.coroutines.delay
import javax.inject.Inject

class LibraryRepoImpl @Inject constructor(): LibraryRepo {

    override suspend fun fetchBooks(): List<Book> {
        delay(3000)
        return BooksDB.books
    }

    override fun borrowBook(id: Int) {
        val ix = BooksDB.books.indexOfFirst { it.id == id }
        if(BooksDB.books[ix].availableCount > 0)
            BooksDB.books[ix].availableCount -= 1
    }

    override fun returnBook(id: Int) {
        val ix = BooksDB.books.indexOfFirst { it.id == id }
        BooksDB.books[ix].availableCount += 1
    }

    override fun updateBook(id: Int) {
//        val index = BooksDB.books.indexOfFirst { it.id == book.id }
//        if (index != -1) { BooksDB.books[index] = book }
    }

    override fun setFavoriteBook(id: Int) {
        val ix = BooksDB.books.indexOfFirst { it.id == id }
        BooksDB.books[ix].isFavorite = BooksDB.books[ix].isFavorite.not()
    }

}