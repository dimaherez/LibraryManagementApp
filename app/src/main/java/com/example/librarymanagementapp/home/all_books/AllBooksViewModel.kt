package com.example.librarymanagementapp.home.all_books

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.data.BooksDB
import com.example.domain.enums.Genre
import com.example.domain.models.Book
import com.example.domain.models.Review
import com.example.domain.use_cases.AddBookUC
import com.example.domain.use_cases.FetchBooksUseCase
import com.example.domain.use_cases.SetFavoriteBookUC
import com.example.librarymanagementapp.home.HomeBaseIntent
import com.example.librarymanagementapp.mvi.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AllBooksViewModel @Inject constructor(
    private val fetchBooksUseCase: FetchBooksUseCase,
    private val setFavoriteBookUC: SetFavoriteBookUC,
    private val addBookUC: AddBookUC
) : ViewModel() {

    private val _uiState = MutableStateFlow<BaseUiState>(BaseUiState.Loading)
    val uiState: StateFlow<BaseUiState> = _uiState

//    private val _uiState = MutableLiveData<BaseUiState>(BaseUiState.Loading)
//    val uiState: LiveData<BaseUiState> = _uiState

    init {
        fetchAllBooks()
    }

    fun processIntent(intent: HomeBaseIntent) {
        when (intent) {
            is AllBooksIntent.FetchAllBooks -> fetchAllBooks()
            is AllBooksIntent.AddBook -> addBook()
            is AllBooksIntent.SetFavorite -> setFavorite(intent.id)
            is AllBooksIntent.ResetFavorite -> resetFavorite(intent.id)
            is HomeBaseIntent.ToggleFavoriteBook -> {
                toggleFavorite(intent.id)
            }
        }
    }

    private fun resetFavorite(id: Int) {
        val currentState = _uiState.value
        if (currentState is AllBooksState.AllBooks) {
            val ix = currentState.books.indexOfFirst { it.id == id }
            if (currentState.books[ix].isFavorite.not()) return
            toggleFavorite(id)
        }
    }

    private fun setFavorite(id: Int) {
        val currentState = _uiState.value
        if (currentState is AllBooksState.AllBooks) {
            val ix = currentState.books.indexOfFirst { it.id == id }
            if (currentState.books[ix].isFavorite) return
            toggleFavorite(id)
        }
    }

    private fun addBook() {
        val currentState = _uiState.value
        if (currentState is AllBooksState.AllBooks) {
            val newBook = Book(
                id = currentState.books.size + 1,
                title = "ATest Book ${BooksDB.books.size + 1}",
                genre = Genre.FICTION,
                author = "Test Author",
                releaseDate = LocalDate.of(1925, 4, 10),
                price = 10.99f,
                isAvailable = true,
                borrowCount = 5,
                availableCount = 5,
                isFavorite = false,
                description = "Some description",
                reviews = listOf(
                    Review("User1", rating = 4, content = "Nice"),
                    Review("User2", rating = 3, content = "Good")
                )
            )

            CoroutineScope(Dispatchers.IO).launch {
                addBookUC.addBook(newBook)
            }

            val currentBooks = currentState.books.toMutableList()
            currentBooks.add(newBook)
            _uiState.value = currentState.copy(
                books = currentBooks
            )
        }
    }


    private fun toggleFavorite(id: Int) {
        Log.d("mylog", "toggleFavorite")
        val currentState = _uiState.value
        if (currentState is AllBooksState.AllBooks) {
            CoroutineScope(Dispatchers.IO).launch {
                setFavoriteBookUC.setFavoriteBook(id)
            }

            val indexById = currentState.books.indexOfFirst { it.id == id }

            _uiState.value = currentState.copy(
                books = currentState.books.toMutableList().also {
                    it[indexById] =
                        it[indexById].copy(isFavorite = it[indexById].isFavorite.not())
                })
        }

    }

    private fun fetchAllBooks() {
//        _uiState.value = BaseUiState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val response = fetchBooksUseCase.fetchBooks()
            if (response != null) {
                _uiState.value = AllBooksState.AllBooks(response)
            } else {
                _uiState.value = BaseUiState.Error("List is null")
            }

        }
    }

//    private fun borrowBook(id: Int) {
//        CoroutineScope(Dispatchers.Main).launch {
//            delay(Random.nextLong(100, 3000))
//
//            val currentState = _uiState.value
//            if (currentState is UiState.Data) {
//                _uiState.value = UiState.Data(
//                    currentState.data.toMutableList().also {
//                        val ix = currentState.data.indexOfFirst { book -> book.id == id }
//                        if (it[ix].availableCount > 0) {
//                            it[ix] = it[ix].copy(availableCount = it[ix].availableCount - 1)
////                            BorrowBookUseCase().borrowBook(it[ix].id)
//                        }
//                    })
//            }
//
//            borrowBookUseCase.borrowBook(id)
//        }
//    }


//    private fun returnBook(id: Int) {
//        CoroutineScope(Dispatchers.Main).launch {
//            delay(Random.nextLong(100, 3000))
//
//            val currentState = _uiState.value
//            if (currentState is UiState.Data) {
//                _uiState.value = UiState.Data(
//                    currentState.data.toMutableList().also {
//                        val ix = currentState.data.indexOfFirst { book -> book.id == id }
//                        it[ix] = it[ix].copy(availableCount = it[ix].availableCount + 1)
////                        ReturnBookUseCase().returnBook(it[ix].id)
//                    })
//            }
//
//            returnBookUseCase.returnBook(id)
//        }
//    }

}