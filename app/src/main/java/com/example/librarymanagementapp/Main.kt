package com.example.librarymanagementapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

val scope = CoroutineScope(Dispatchers.IO)

suspend fun main() = runBlocking {
    val totalPages = 7
    val pageSize = 6

    fetchAllBooks(totalPages, pageSize)

    withContext(Dispatchers.IO) {
        Thread.sleep(totalPages * 3000L)
    }

//    println(
//        "\n\nAdded a book: ${
//            LibraryDB.addBook(
//                Book(
//                    id = (books?.size?.plus(1)) ?: 1,
//                    title = "To Kill a Mockingbird",
//                    genre = Genre.FICTION,
//                    author = "Harper Lee",
//                    releaseDate = LocalDate.of(1960, 7, 11),
//                    price = 12.99f,
//                    isAvailable = true,
//                    borrowCount = 0,
//                    availableCount = 5
//                )
//            )
//        }"
//    )
}

suspend fun fetchAllBooks(totalPages: Int, pageSize: Int) {
    scope.launch {
        val result = (0 until totalPages)
            .map { page -> async { LibraryDB.getBooksByRange(page*pageSize, (page+1)*pageSize) } }

        val pagesWithBooks = awaitAll(*result.toTypedArray())

        pagesWithBooks.filterNotNull().forEachIndexed { index, books ->
            println("\nPage $index :")
            books.forEach(::println)
         }
    }
}






