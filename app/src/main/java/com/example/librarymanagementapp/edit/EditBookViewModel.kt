package com.example.librarymanagementapp.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.domain.models.Book
import com.example.domain.use_cases.FetchBookByIdUC
import com.example.domain.use_cases.UpdateBookUC
import com.example.librarymanagementapp.mvi.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EditBookViewModel @Inject constructor(
    private val fetchBookByIdUC: FetchBookByIdUC,
    private val updateBookUC: UpdateBookUC
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun processIntent(intent: EditBookIntent) {
        when (intent) {
            is EditBookIntent.FetchBookById -> fetchBookById(intent.id)
            is EditBookIntent.UpdateBook -> updateBook(intent.book)
        }
    }

    private fun updateBook(book: Book) {
        updateBookUC.updateBook(book)
        fetchBookById(book.id)
    }


    private fun fetchBookById(id: Int) {
        Log.d("mylog", "fetchBookById $id")
        _uiState.value = UiState.Loading
        _uiState.value = UiState.BookData(fetchBookByIdUC.fetchBookById(id))

    }
}