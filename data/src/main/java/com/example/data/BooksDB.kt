package com.example.data

import com.example.domain.enums.Genre
import com.example.domain.models.Book
import java.time.LocalDate
import kotlin.random.Random

object BooksDB {
    val books = initBooks()

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
}