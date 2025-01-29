package com.example.librarymanagementapp.mvp

import com.example.librarymanagementapp.LibraryDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Presenter {

    private var view: ShowBookView? = null

    fun attachView(mvpFragment: MVPFragment) {
        view = mvpFragment
    }

    fun detachView() {
        view = null
    }

    fun loadBooks() {
        println("qweqwe loadBooks $view")
        view?.showLoading()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                println("qweqwe  try { $view")
                val books = LibraryDB.loadBooks()
                view?.showBooks(books!!)
            } catch (e: Exception) {
                view?.showError(e.message ?: "An error occurred")
            }
        }
    }

}