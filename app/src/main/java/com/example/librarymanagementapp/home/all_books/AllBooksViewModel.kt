package com.example.librarymanagementapp.home.all_books

import androidx.lifecycle.ViewModel
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
            is HomeBaseIntent.SetFavoriteBook -> {
                setFavoriteBook(intent.id)
            }
        }
    }

    private fun addBook() {
        val currentState = _uiState.value
        if (currentState !is AllBooksState.AllBooks) return

        CoroutineScope(Dispatchers.Main).launch {
            addBookUC.addBook()?.let { newBook ->
                _uiState.value = currentState.copy(
                    books = currentState.books.toMutableList().apply {
                        add(newBook)
                    }
                )
            }

            fetchAllBooks()
        }
    }


    private fun setFavoriteBook(id: Int) {
        val currentState = _uiState.value
        CoroutineScope(Dispatchers.Main).launch { // Dispatchers.IO not working
            if (currentState is AllBooksState.AllBooks) {
                val ix = currentState.books.indexOfFirst { it.id == id }

                _uiState.value = currentState.copy(
                    books = currentState.books.toMutableList()
                        .also { list -> list[ix] = list[ix].copy(isFavorite = list[ix].isFavorite.not()) }
                )

                setFavoriteBookUC.setFavoriteBook(id)
            }
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