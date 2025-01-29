package com.example.librarymanagementapp.mvp

import com.example.librarymanagementapp.LibraryDB
import com.example.librarymanagementapp.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Presenter {

    private var view: ((UiState) -> Unit)? = null

    fun attachView(view: (UiState) -> Unit) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

    fun loadBooks() {
        view?.invoke(UiState.Loading)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val books = withContext(Dispatchers.IO) {
                    LibraryDB.loadBooks()
                }
                view?.invoke(UiState.Data(books!!))
            } catch (e: Exception) {
                view?.invoke(UiState.Error(e.message ?: "An error occurred"))
            }
        }
    }

}