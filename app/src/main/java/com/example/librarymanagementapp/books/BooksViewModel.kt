package com.example.librarymanagementapp.books

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.domain.use_cases.FetchBooksUseCase
import com.example.domain.use_cases.SetFavoriteBookUC
import com.example.librarymanagementapp.mvi.LoadingStatus
import com.example.librarymanagementapp.mvi.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val fetchBooksUseCase: FetchBooksUseCase,
    private val setFavoriteBookUC: SetFavoriteBookUC
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        processIntent(BooksIntent.FetchData)
    }

    fun processIntent(intent: BooksIntent) {
        when (intent) {
            is BooksIntent.FetchData -> fetchBooks()
            is BooksIntent.SetFavoriteBook -> {
                setFavoriteBook(intent.id)
            }
        }
    }

    private fun setFavoriteBook(id: Int) {
        val currentState = _uiState.value
        if (currentState is UiState.Data) {
            val ix = currentState.data.indexOfFirst { it.id == id }

            _uiState.value = UiState.Data(
                currentState.data.toMutableList()
                    .also { list -> list[ix] = list[ix].copy(isFavorite = list[ix].isFavorite.not()) }
            )

            setFavoriteBookUC.setFavoriteBook(id)
        }
    }

    fun fetchBooks() {
        _uiState.value = UiState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val response = fetchBooksUseCase.fetchBooks()
            if (response != null) {
                _uiState.value = UiState.Data(response)
            } else {
                _uiState.value = UiState.Error("List is null")
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