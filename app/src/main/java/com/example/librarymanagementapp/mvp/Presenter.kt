package com.example.librarymanagementapp.mvp

import android.util.Log
import com.example.librarymanagementapp.LibraryDB
import com.example.librarymanagementapp.models.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

object Presenter {

    private var view: BooksView? = null

    fun attachView(mvpFragment: MVPFragment) {
        view = mvpFragment
    }

    fun detachView() {
        view = null
    }

    fun fetchBooks() {
        view?.showLoading()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val books = LibraryDB.loadBooks()
                Log.d("mylog", books.toString())
                view?.showBooks(books!!)
            } catch (e: Exception) {
                view?.showBooks(emptyList())
                view?.showError(e.message ?: "Books list is null")
            }
        }
    }

    fun toggleFavorite(book: Book) {
        LibraryDB.toggleFavorite(book)
    }

//    fun filterBooks(query: String?) {
//        if (query.isNullOrEmpty()) return
//
//        view?.showLoading()
//        CoroutineScope(Dispatchers.Main).launch {
//            try {
//                val books = LibraryDB.filterBooks(query)
//                view?.showBooks(books!!)
//            } catch (e: Exception) {
//                view?.showError("Filtered list is null")
//            }
//        }
//    }


}