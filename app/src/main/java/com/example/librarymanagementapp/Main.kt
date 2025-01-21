package com.example.librarymanagementapp

fun main() {

    println(loadBooks())

    println(
        "\n\nAdded a book: ${
            LibraryDB.addBook(
                Book(
                    "The Hobbit",
                    Genre.FANTASY,
                    " J . R . R . Tolkien "
                )
            )
        }"
    )

    println("\n\nFound a book by title: ${LibraryDB.findBookByTitle("The Shining")}")
    println("Found a book by genre ${LibraryDB.findBookByGenre(Genre.FANTASY)}")

    println("\n\nBorrowed a book: ${LibraryDB.borrowBook("The Hobbit")}")
    println("Returned a book: ${LibraryDB.returnBook("The Hobbit")}")
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

