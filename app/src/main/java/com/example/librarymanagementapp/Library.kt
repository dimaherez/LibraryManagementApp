package com.example.librarymanagementapp

data class Book(
    val title: String,
    val genre: Genre,
    val author: String
)

enum class Genre {
    ROMANCE,
    FANTASY,
    SCIENCE_FICTION,
    PARANORMAL,
    HORROR,
}

object LibraryDB {
    private val books = mutableListOf(
        Book("Harry Potter and the Sorcerer's Stone", Genre.FANTASY, "J.K. Rowling"),
        Book("Dune", Genre.SCIENCE_FICTION, "Frank Herbert"),
        Book("The Da Vinci Code", Genre.HORROR, "Dan Brown"),
        Book("The Shining", Genre.HORROR, "Stephen King"),
        Book("The Girl with the Dragon Tattoo", Genre.FANTASY, "Stieg Larsson"),
        Book("Normal People", Genre.SCIENCE_FICTION, "Sally Rooney")
    )

    private val borrowedBooks: MutableList<Book> = mutableListOf()

    fun getAvailableBooks(): List<Book>? = books

    fun addBook(book: Book): Book {
        books.add(book)
        return book
    }

    fun findBookByTitle(title: String): Book? =
        books.find { it.title.lowercase() == title.lowercase() }

    fun findBookByGenre(genre: Genre): List<Book> = books.filter { it.genre == genre }

    fun borrowBook(title: String): Book? {
        val book = findBookByTitle(title)

        if (book !== null) {
            books.remove(book)
            borrowedBooks.add(book)
        }
        return book
    }

    fun returnBook(title: String): Book? {
        val book = borrowedBooks.find { it.title == title }

        if (book !== null) {
            borrowedBooks.remove(book)
            books.add(book)
        }

        return book
    }
}

