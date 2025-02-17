package com.example.librarymanagementapp.info

import androidx.lifecycle.ViewModel
import com.example.domain.use_cases.FetchBookByIdUC
import com.example.librarymanagementapp.mvi.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BookInfoViewModel @Inject constructor (
    private val fetchBookByIdUC: FetchBookByIdUC
) : ViewModel() {

    private val _uiState = MutableStateFlow<BaseUiState>(BaseUiState.Loading)
    val uiState: StateFlow<BaseUiState> = _uiState

    fun processIntent(intent: BookInfoIntent) {
        when(intent){
            is BookInfoIntent.FetchBookById -> fetchBookById(intent.id)
        }
    }


    private fun fetchBookById(bookId: Int) {
        _uiState.value = BaseUiState.Loading
        val response = fetchBookByIdUC.fetchBookById(bookId)
        if (response != null)
            _uiState.value = BookInfoState.BookData(response)
        else
            _uiState.value = BaseUiState.Error("Book not found")
    }
}