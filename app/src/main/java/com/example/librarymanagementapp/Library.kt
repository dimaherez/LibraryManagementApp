package com.example.librarymanagementapp

import java.time.LocalDate
import java.time.LocalDateTime

data class Book(
    val title: String,
    val genre: Genre,
    val author: String,
    val releaseDate: LocalDate,
    val price: Float,
    var isAvailable: Boolean,
    var borrowCount: Int,
    var availableCount: Int
)

data class BookOrder(
    val book: Book,
    val orderDateTime: LocalDateTime
)

enum class Genre {
    FICTION,
    FANTASY,
    SCIENCE_FICTION,
    MYSTERY,
    BIOGRAPHY,
}

object LibraryDB {
    private val books = initBooks()
    private val bookOrders = initBookOrders()

    fun getAvailableBooks(): List<Book>? = books.filter { it.isAvailable }

    fun addBook(book: Book): Book {
        books.add(book)
        return book
    }

    fun findBookByTitle(title: String): Book? =
        books.find { it.title.equals(title, ignoreCase = true) }

    fun findBooksByGenre(genre: Genre): List<Book> = books.filter { it.genre == genre }

    fun borrowBook(title: String): Book? {
        val book = findBookByTitle(title)

        if (book !== null) {
            book.borrowCount += 1
            book.availableCount -= 1
            book.isAvailable = (book.availableCount != 0)

            bookOrders.add(BookOrder(book, LocalDateTime.now()))
        }
        return book
    }

    fun returnBook(title: String): Book? {
        val book = books.find { it.title == title }

        if (book?.isAvailable?.not() == true) {
            book.availableCount += 1
            book.isAvailable = true
        }

        return book
    }

    // Group books by Genre
    fun groupByGenre(): Map<Genre, List<Book>> = books.groupBy { it.genre }

    // Group books by availability
    private fun groupByAvailability() = books.groupBy { it.isAvailable }

    //Add sorting by title, release date, or price. For each sorting order show available first
    fun sortedByTitleAscending() = groupByAvailability().flatMap { entry -> entry.value.sortedBy { it.title } }
    fun sortedByTitleDescending() = groupByAvailability().flatMap { entry -> entry.value.sortedByDescending { it.title } }

    fun sortedByReleaseDateAscending() = groupByAvailability().flatMap { entry -> entry.value.sortedBy { it.releaseDate } }
    fun sortedByReleaseDateDescending() = groupByAvailability().flatMap { entry -> entry.value.sortedByDescending { it.releaseDate } }

    fun sortedByPriceAscending() = groupByAvailability().flatMap { entry -> entry.value.sortedBy { it.price } }
    fun sortedByPriceDescending() = groupByAvailability().flatMap { entry -> entry.value.sortedByDescending { it.price } }

    // Count total books by genre
    fun countByGenre(genre: Genre) = books.count { it.genre == genre }

    // Count total books by author
    fun countByAuthor(author: String) = books.count { it.author == author }

    // show most popular(borrow count) books
    fun mostPopular() = books.sortedBy { it.borrowCount }.takeLast(3)

    // Filter books by author
    fun filterByAuthor(author: String) = books.filter { it.author == author }

    // Filter books by availability
    fun filterByAvailability(isAvailable: Boolean) = books.filter { it.isAvailable == isAvailable }

    // Extract all unique authors
    fun getUniqueAuthors() = books.map { it.author }.toSet()  // distinct()

    // Create a summary report for borrowed books count by genre
    fun reportByGenre(): Map<Genre, Int> = books.groupingBy { it.genre }.eachCount()

    // trending(last 5 min or another testable time) author
    fun trendingAuthor(): String {
        return bookOrders
            .filter { it.orderDateTime.isAfter(LocalDateTime.now().minusMinutes(5)) }
            .groupingBy { it.book.author }
            .eachCount()
            .maxBy { it.value }
            .key
    }


    private fun initBooks(): MutableList<Book> {
        return mutableListOf(
            Book(
                title = "The Great Gatsby",
                genre = Genre.FICTION,
                author = "F. Scott Fitzgerald",
                releaseDate = LocalDate.of(1925, 4, 10),
                price = 10.99f,
                isAvailable = true,
                borrowCount = 5,
                availableCount = 5
            ),
            Book(
                title = "The Da Vinci Code",
                genre = Genre.MYSTERY,
                author = "Dan Brown",
                releaseDate = LocalDate.of(2003, 3, 18),
                price = 15.99f,
                isAvailable = true,
                borrowCount = 12,
                availableCount = 5
            ),
            Book(
                title = "A Brief History of Time",
                genre = Genre.FICTION,
                author = "Stephen Hawking",
                releaseDate = LocalDate.of(1988, 6, 1),
                price = 20.99f,
                isAvailable = false,
                borrowCount = 8,
                availableCount = 5
            ),
            Book(
                title = "Harry Potter and the Philosopher's Stone",
                genre = Genre.FANTASY,
                author = "J.K. Rowling",
                releaseDate = LocalDate.of(1997, 6, 26),
                price = 25.99f,
                isAvailable = true,
                borrowCount = 20,
                availableCount = 5
            ),
            Book(
                title = "Dune",
                genre = Genre.SCIENCE_FICTION,
                author = "Frank Herbert",
                releaseDate = LocalDate.of(1965, 8, 1),
                price = 18.99f,
                isAvailable = true,
                borrowCount = 15,
                availableCount = 5
            ),
            Book(
                title = "Steve Jobs",
                genre = Genre.BIOGRAPHY,
                author = "Walter Isaacson",
                releaseDate = LocalDate.of(2011, 10, 24),
                price = 30.99f,
                isAvailable = false,
                borrowCount = 10,
                availableCount = 5
            ),
            Book(
                title = "Harry Potter and the Chamber of Secrets",
                genre = Genre.FANTASY,
                author = "J.K. Rowling",
                releaseDate = LocalDate.of(1998, 7, 2),
                price = 26.99f,
                isAvailable = true,
                borrowCount = 18,
                availableCount = 5
            )
        )
    }

    private fun initBookOrders(): MutableList<BookOrder> {
        return mutableListOf(
            BookOrder(books.random(), LocalDateTime.now().minusMinutes(10)),
            BookOrder(books.random(), LocalDateTime.now().minusMinutes(7)),
            BookOrder(books.random(), LocalDateTime.now().minusMinutes(5)),
            BookOrder(books.random(), LocalDateTime.now().minusMinutes(3)),
            BookOrder(books.random(), LocalDateTime.now().minusMinutes(3)),
            BookOrder(books.random(), LocalDateTime.now().minusMinutes(2)),
        )
    }
}

