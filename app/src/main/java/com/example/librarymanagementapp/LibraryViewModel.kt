package com.example.librarymanagementapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class LibraryViewModel: ViewModel() {
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    private val _sorted = MutableLiveData<List<Book>>()
    val sorted: LiveData<List<Book>> get() = _sorted


    private suspend fun fetchBooks() {
        handleState(State.Loading)

        val response = LibraryDB.loadBooks()

        if (response.isNullOrEmpty()) {
            handleState(State.Error("Data is empty or null"))
        } else {
            handleState(State.Data(response))
            _books.postValue(response!!)
        }
    }

    private suspend fun getSortedList() {
        handleState(State.Loading)

        val response = LibraryDB.sortedByTitleAscending()

        if (response.isNullOrEmpty()) {
            handleState(State.Error("Data is empty or null"))
        } else {
            handleState(State.Data(response))
            _sorted.postValue(response!!)
        }
    }

    fun parallelCall() {
        viewModelScope.launch {
            val deferred1: Deferred<List<Book>> = async { LibraryDB.loadBooks()!! }
            val deferred2: Deferred<List<Book>> = async { LibraryDB.sortedByTitleAscending()!! }

            val result = awaitAll(deferred1, deferred2)

            _books.postValue(result[0])
            _sorted.postValue(result[1])
        }

    }

    fun nonParallelCall() {
        viewModelScope.launch {
            fetchBooks()
            getSortedList()
        }
    }

    private fun handleState(state: State<List<Book>>) {
        when (state) {
            is State.Loading -> {
                Log.d("mylog", "Loading...")
            }

            is State.Data -> {
                Log.d("mylog", "Success!")
            }

            is State.Error -> {
                Log.d("mylog", state.message)
            }
        }
    }
}