package com.example.librarymanagementapp.mvvm

import androidx.lifecycle.ViewModel
import com.example.librarymanagementapp.LibraryDB
import com.example.librarymanagementapp.UiState
import com.example.librarymanagementapp.enums.Genre
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelMVVM: ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState


    fun fetchBooks() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = LibraryDB.loadBooks()

            if (response != null){
                _uiState.value = UiState.Data(response)
            } else {
                _uiState.value = UiState.Error("List is null")
            }

        }
    }

    fun sortBooks() {
        CoroutineScope(Dispatchers.IO).launch {
            val currentState = _uiState.value
            if (currentState is UiState.Data){
                _uiState.value = currentState.copy(data = currentState.data.sortedBy { it.title })
            }

        }
    }

    fun filterBooks() {
        CoroutineScope(Dispatchers.IO).launch {
            val currentState = _uiState.value
            if (currentState is UiState.Data){
                _uiState.value = currentState.copy(data = currentState.data.filter { it.genre == Genre.FANTASY })
            }
        }
    }
}