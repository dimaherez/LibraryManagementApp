package com.example.librarymanagementapp.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.librarymanagementapp.LibraryDB
import com.example.librarymanagementapp.LibraryDB.isUserSignedIn
import com.example.librarymanagementapp.UiState
import com.example.librarymanagementapp.enums.Genre
import com.example.librarymanagementapp.models.Book
import com.example.librarymanagementapp.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelMVVM: ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState


    private val _data = MutableLiveData<List<Book>>(emptyList())
    val data = _data as LiveData<List<Book>>

    private val _dataUsers = MutableLiveData<List<User>>(emptyList())
    val dataUsers = _dataUsers as LiveData<List<User>>

    private val _loading = MutableLiveData(true)
    val loading = _loading as LiveData<Boolean>




    fun fetchBooks() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = LibraryDB.loadBooks()

            if (response != null){
                _uiState.value = UiState.Data(response)
                _loading.value = false
                _data.value = response!!
            } else {
                _loading.value = false
                _data.value = emptyList()
                _uiState.value = UiState.Error("List is null")
            }

        }
    }

// only for signed in users
    fun fetchUser() {
        if (isUserSignedIn.not()) return

        CoroutineScope(Dispatchers.IO).launch {
            val response = LibraryDB.loadUsers()

            if (response.isNotEmpty()){
                _loading.value = false
                _dataUsers.value = response!!
            } else {
                _loading.value = false
                _dataUsers.value = emptyList()
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