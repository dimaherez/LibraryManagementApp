package com.example.data

import com.example.domain.enums.Genre
import com.example.domain.models.Book
import com.example.domain.models.BookOrder
import com.example.domain.models.Review
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

object BooksDB {
    val books = initBooks()
    val bookOrders = initBookOrders()

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
                availableCount = 5,
                isFavorite = true,
                description = "A novel set in the Roaring Twenties, exploring themes of wealth, love, and the American Dream.",
                reviews = listOf(
                    Review("User1", rating = 4, content = "Nice"),
                    Review("User2", rating = 3, content = "Good")
                )
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
                availableCount = 5,
                description = "A mystery thriller that follows Robert Langdon as he uncovers secrets hidden in works of art and religious history."
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
                availableCount = 5,
                isFavorite = true,
                description = "An exploration of cosmology and theoretical physics, offering insights into the nature of the universe."
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
                availableCount = 5,
                isFavorite = true,
                description = "The first book in the Harry Potter series, introducing young wizard Harry and his adventures at Hogwarts."
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
                availableCount = 5,
                description = "A science fiction epic set on the desert planet Arrakis, focusing on politics, religion, and power struggles."
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
                availableCount = 5,
                description = "A biography of Apple co-founder Steve Jobs, detailing his life, innovations, and impact on technology."
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
                availableCount = 5,
                description = "The second book in the Harry Potter series, where Harry returns to Hogwarts and faces new challenges."
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
            BookOrder(
                books.random(),
                LocalDateTime.now().minusMinutes(10)
            ),
            BookOrder(
                books.random(),
                LocalDateTime.now().minusMinutes(7)
            ),
            BookOrder(
                books.random(),
                LocalDateTime.now().minusMinutes(5)
            ),
            BookOrder(
                books.random(),
                LocalDateTime.now().minusMinutes(3)
            ),
            BookOrder(
                books.random(),
                LocalDateTime.now().minusMinutes(3)
            ),
            BookOrder(
                books.random(),
                LocalDateTime.now().minusMinutes(2)
            ),
        )
    }
}