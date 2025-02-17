package com.example.librarymanagementapp.edit

import androidx.lifecycle.ViewModel
import com.example.domain.models.Book
import com.example.domain.use_cases.FetchBookByIdUC
import com.example.domain.use_cases.UpdateBookUC
import com.example.librarymanagementapp.mvi.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EditBookViewModel @Inject constructor(
    private val fetchBookByIdUC: FetchBookByIdUC,
    private val updateBookUC: UpdateBookUC
) : ViewModel() {

    private val _uiState = MutableStateFlow<BaseUiState>(BaseUiState.Loading)
    val uiState: StateFlow<BaseUiState> = _uiState

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
        _uiState.value = BaseUiState.Loading
        val response = fetchBookByIdUC.fetchBookById(id)
        if (response != null)
            _uiState.value = BookEditState.BookData(response)
        else
            _uiState.value = BaseUiState.Error("Book not found")

    }
}