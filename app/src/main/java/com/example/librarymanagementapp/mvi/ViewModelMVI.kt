package com.example.librarymanagementapp.mvi

import androidx.lifecycle.ViewModel
import com.example.librarymanagementapp.LibraryDB
import com.example.librarymanagementapp.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelMVI : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun processIntent(intent: MyIntent) {
        when (intent) {
            is MyIntent.FetchData -> fetchBooks()
            is MyIntent.BorrowBook -> borrowBook()
            is MyIntent.ReturnBook -> returnBook()
        }
    }

    private fun fetchBooks() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = LibraryDB.loadBooks()

            if (response != null) {
                _uiState.value = UiState.Data(response.filter { it.borrowCount > 0 })
            } else {
                _uiState.value = UiState.Error("List is null")
            }

        }
    }

    private fun borrowBook() {
        val currentState = _uiState.value
        if (currentState is UiState.Data) {
            currentState.data.mapIndexed { index, book ->
                if (index == 0) {
                    book.borrowCount += 1
                    return
                }
            }
        }
    }

    private fun returnBook() {
        val currentState = _uiState.value
        if (currentState is UiState.Data) {
            currentState.data.mapIndexed { index, book ->
                if (index == 0) {
                    book.borrowCount -= 1
                    return
                }
            }
        }
    }

}