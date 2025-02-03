package com.example.librarymanagementapp

import com.example.librarymanagementapp.enums.Genre
import com.example.librarymanagementapp.models.Book
import com.example.librarymanagementapp.models.BookOrder
import com.example.librarymanagementapp.models.User
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

object LibraryDB {
    private val books = initBooks()
    private val bookOrders = initBookOrders()
    var isUserSignedIn = false

    suspend fun loadBooks(): List<Book>? {
        delay(2000)
//        return books
        return if (Random.nextBoolean()) books else null
    }

    suspend fun loadUsers(): List<User> {
        delay(2000)
        return if (Random.nextBoolean()) listOf(User()) else emptyList()
    }

    suspend fun getBookOrders(): List<BookOrder>? {
        delay(2000)
        return bookOrders
    }

    fun updateBook(book: Book) {
        val index = books.indexOfFirst { it.id == book.id }
        if (index != -1) { books[index] = book }
    }



    suspend fun getBooksByRange(start: Int, end: Int): List<Book>? {
        delay(Random.nextLong(100, 3000))

        return try {
            books.subList(start, end)
        } catch (e: IndexOutOfBoundsException) {
            if (start == books.size) {
                return listOf(books.last())
            } else if (start > books.size) {
                return null
            }
            val newEnd = books.size % end
            books.subList(start, newEnd)
        }
    }

    fun addBook(book: Book): Book {
        books.add(book)
        return book
    }

    fun toggleFavorite(book: Book) {
        val ix = books.indexOfFirst { it.id == book.id }
        books[ix].isFavorite = book.isFavorite

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

    fun returnBook(id: Int) {
        val ix = books.indexOfFirst { it.id == id }
        books[ix].availableCount += 1
    }

    fun borrowBook(id: Int) {
        val ix = books.indexOfFirst { it.id == id }
        if(books[ix].availableCount > 0)
            books[ix].availableCount -= 1
    }

    // Group books by Genre
    fun groupByGenre(): Map<Genre, List<Book>> = books.groupBy { it.genre }


    // Group books by availability
    private fun groupByAvailability() = books.groupBy { it.isAvailable }

    //Add sorting by title, release date, or price. For each sorting order show available first
    suspend fun sortedByTitleAscending(): List<Book>? {
        delay(3000)
        return groupByAvailability().flatMap { entry -> entry.value.sortedBy { it.title } }
    }

    suspend fun sortedByTitleDescending() =
        groupByAvailability().flatMap { entry -> entry.value.sortedByDescending { it.title } }

    suspend fun sortedByReleaseDateAscending() =
        groupByAvailability().flatMap { entry -> entry.value.sortedBy { it.releaseDate } }

    suspend fun sortedByReleaseDateDescending() =
        groupByAvailability().flatMap { entry -> entry.value.sortedByDescending { it.releaseDate } }

    suspend fun sortedByPriceAscending() =
        groupByAvailability().flatMap { entry -> entry.value.sortedBy { it.price } }

    suspend fun sortedByPriceDescending() =
        groupByAvailability().flatMap { entry -> entry.value.sortedByDescending { it.price } }

    // Count total books by genre
    fun countByGenre(genre: Genre) = books.count { it.genre == genre }


    // Count total books by author
    fun countByAuthor(author: String) = books.count { it.author == author }

    // show most popular(borrow count) books
    fun mostPopular() = books.sortedBy { it.borrowCount }.takeLast(3)

    // Filter books by author
    suspend fun filterByAuthor(author: String) = books.filter { it.author == author }

    // Filter books by availability
    suspend fun filterByAvailability(): List<Book> {
        delay(1000)
        return books.filter { it.isAvailable }
    }

    fun filterBooks(query: String): List<Book>? {
        return books.filter {
            it.title.lowercase().contains(query.lowercase()) ||
                    it.author.lowercase().contains(query.lowercase())
        }
    }

    // Extract all unique authors
    fun getUniqueAuthors() = books.map { it.author }.toSet()  // distinct()

    // Create a summary report for borrowed books count by genre
    suspend fun reportByGenre(): Map<Genre, Int> {
        delay(3000)
        return books.groupingBy { it.genre }.eachCount()
    }

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
        val books = mutableListOf(
            Book(
                id = 1,
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
                id = 2,
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
                id = 3,
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
                id = 4,
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
                id = 5,
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
                id = 6,
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
                id = 7,
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

//        books.addAll(generateRandomBooks(30))

        return books
    }

    private fun generateRandomBooks(n: Int): List<Book> {
        fun randomDate(): LocalDate {
            val startEpochDay = LocalDate.of(1900, 1, 1).toEpochDay()
            val endEpochDay = LocalDate.of(2025, 12, 31).toEpochDay()
            val randomDay = Random.nextLong(startEpochDay, endEpochDay)
            return LocalDate.ofEpochDay(randomDay)
        }

        return List(n) { index ->
            Book(
                id = index + 10,
                title = "Title $index",
                genre = Genre.values().random(),
                author = "Author $index",
                releaseDate = randomDate(),
                price = Random.nextFloat() * 100,
                isAvailable = Random.nextBoolean(),
                borrowCount = Random.nextInt(0, 100),
                availableCount = Random.nextInt(0, 20)
            )
        }
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

