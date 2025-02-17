package com.example.librarymanagementapp.home.all_books

import androidx.lifecycle.ViewModel
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
    private val setFavoriteBookUC: SetFavoriteBookUC
) : ViewModel() {

    private val _uiState = MutableStateFlow<BaseUiState>(BaseUiState.Loading)
    val uiState: StateFlow<BaseUiState> = _uiState

    init {
        fetchAllBooks()
    }

    fun processIntent(intent: HomeBaseIntent) {
        when (intent) {
            is AllBooksIntent.FetchAllBooks -> fetchAllBooks()
            is HomeBaseIntent.SetFavoriteBook -> {
                setFavoriteBook(intent.id)
            }
        }
    }

    private fun setFavoriteBook(id: Int) {
        val currentState = _uiState.value
        if (currentState is AllBooksState.Books) {
            val ix = currentState.books.indexOfFirst { it.id == id }

            _uiState.value = AllBooksState.Books(
                currentState.books.toMutableList()
                    .also { list -> list[ix] = list[ix].copy(isFavorite = list[ix].isFavorite.not()) }
            )

            setFavoriteBookUC.setFavoriteBook(id)
        }
    }

    private fun fetchAllBooks() {
        _uiState.value = BaseUiState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val response = fetchBooksUseCase.fetchBooks()
            if (response != null) {
                _uiState.value = AllBooksState.Books(response)
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


//        val currentState = _uiState.value
//        if (currentState is UiState.Data) {
//            _uiState.value = UiState.Data(
//                currentState.data.toMutableList().also {
//                    if (it[0].availableCount > 0) {
//                        it[0] = it[0].copy(availableCount = it[0].availableCount - 1)
//                    }
//                    LibraryDB.updateBook(it[0])
//                }
//            )
//        }


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


//        val currentState = _uiState.value
//        if (currentState is UiState.Data) {
//            _uiState.value = UiState.Data(
//                currentState.data.toMutableList().also {
//                    it[0] = it[0].copy(availableCount = it[0].availableCount + 1)
//                }
//            )
////            currentState.data.forEachIndexed { index, book ->
////                if (index == 0) {
////                    book.borrowCount -= 1
//////                    _uiState.emit(UiState.Loading)
//////                    delay(150)
////                    _uiState.value = currentState.copy(data = currentState.data.toMutableList())
////                    return@forEachIndexed
////                }
////            }
//        }
//    }

}