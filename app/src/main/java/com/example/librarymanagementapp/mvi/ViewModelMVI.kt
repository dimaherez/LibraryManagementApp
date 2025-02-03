package com.example.librarymanagementapp.mvi

import androidx.lifecycle.ViewModel
import com.example.librarymanagementapp.LibraryDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class ViewModelMVI : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading(LoadingStatus.INITIAL))
    val uiState: StateFlow<UiState> = _uiState

    init {
        processIntent(MyIntent.FetchData)
    }

    fun processIntent(intent: MyIntent) {
        when (intent) {
            is MyIntent.FetchData -> fetchBooks()
            is MyIntent.BorrowBook -> borrowBook(intent.id)
            is MyIntent.ReturnBook -> returnBook(intent.id)
        }
    }

    fun fetchBooks() {
        if (_uiState.value is UiState.Error) {
            _uiState.value = UiState.Loading(LoadingStatus.AFTER_ERROR) // Loading is after error while fetching data
        } else {
            _uiState.value = UiState.Loading(LoadingStatus.AFTER_SUCCESS) // Loading is after successful fetching data
        }
        CoroutineScope(Dispatchers.IO).launch {
            val response = LibraryDB.loadBooks()
            if (response != null) {
                _uiState.value = UiState.Data(response.toList())
            } else {
                _uiState.value = UiState.Error("List is null")
            }

        }
    }

    private fun borrowBook(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(Random.nextLong(100, 3000))

            val currentState = _uiState.value
            if (currentState is UiState.Data) {
                _uiState.value = UiState.Data(
                    currentState.data.toMutableList().also {
                        val ix = currentState.data.indexOfFirst { book -> book.id == id }
                        if (it[ix].availableCount > 0) {
                            it[ix] = it[ix].copy(availableCount = it[ix].availableCount - 1)
                            LibraryDB.updateBook(it[ix])
                        }
                    })
            } else {
                LibraryDB.borrowBook(id)
            }
        }
    }


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
//
//        }


    private fun returnBook(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(Random.nextLong(100, 3000))

            val currentState = _uiState.value
            if (currentState is UiState.Data) {
                _uiState.value = UiState.Data(
                    currentState.data.toMutableList().also {
                        val ix = currentState.data.indexOfFirst { book -> book.id == id }
                        it[ix] = it[ix].copy(availableCount = it[ix].availableCount + 1)
                        LibraryDB.updateBook(it[ix])
                    })
            } else {
                LibraryDB.returnBook(id)
            }
        }


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


    }

}