package com.example.librarymanagementapp

import java.time.LocalDate

fun main() {

    loadBooks()

    println(
        "\n\nAdded a book: ${
            LibraryDB.addBook(
                Book(
                    title = "To Kill a Mockingbird",
                    genre = Genre.FICTION,
                    author = "Harper Lee",
                    releaseDate = LocalDate.of(1960, 7, 11),
                    price = 12.99f,
                    isAvailable = true,
                    borrowCount = 0,
                    availableCount = 5
                )
            )
        }"
    )

//    println("\n\nFound a book by title: ${LibraryDB.findBookByTitle("The Shining")}")
//    println("Found a book by genre ${LibraryDB.findBookByGenre(Genre.FANTASY)}")
//
//    println("\n\nBorrowed a book: ${LibraryDB.borrowBook("The Hobbit")}")
//    println("Returned a book: ${LibraryDB.returnBook("The Hobbit")}")


//    println("Sorted By Title: ${LibraryDB.sortedByTitleAscending().forEach(::println)}")

//    println("Report by genre: ${LibraryDB.reportByGenre()}")

//    println("Trending author: ${LibraryDB.trendingAuthor()}")

}

fun loadBooks() {
    handleState(State.Loading)

    val response = LibraryDB.getAvailableBooks()

    val result = if (response !== null) {
        State.Data(response)
    } else {
        State.Error("Something went wrong...")
    }

    handleState(result)
}

fun handleState(state: State) {
    when (state) {
        is State.Loading -> println("Loading...")
        is State.Data -> {
            println("Success! Data loaded:")
            state.data.forEach(::println)
        }
        is State.Error -> println(state.msg)
    }
}

sealed class State {
    data object Loading : State()
    data class Data(val data: List<Book>) : State()
    class Error(val msg: String) : State()
}

