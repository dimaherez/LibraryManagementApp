package com.example.librarymanagementapp.home.all_books

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.domain.use_cases.FetchBooksGroupedByInitialUC
import com.example.domain.use_cases.FetchBooksUseCase
import com.example.domain.use_cases.SetFavoriteBookUC
import com.example.librarymanagementapp.home.HomeBaseIntent
import com.example.librarymanagementapp.mvi.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllBooksViewModel @Inject constructor(
    private val fetchBooksUseCase: FetchBooksUseCase,
    private val setFavoriteBookUC: SetFavoriteBookUC,
    private val groupedByInitialUC: FetchBooksGroupedByInitialUC
) : ViewModel() {

    private val _uiState = MutableStateFlow<BaseUiState>(BaseUiState.Loading)
    val uiState: StateFlow<BaseUiState> = _uiState

    init {
        fetchGroupedBooksByInitial()
    }

    fun processIntent(intent: HomeBaseIntent) {
        when (intent) {
            is AllBooksIntent.FetchAllBooks -> fetchGroupedBooksByInitial()
            is HomeBaseIntent.SetFavoriteBook -> {
                setFavoriteBook(intent.id)
            }
        }
    }

    private fun fetchGroupedBooksByInitial() {
//        _uiState.value = BaseUiState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val response = groupedByInitialUC.fetchBooksGroupedByInitial()
            if (response != null) {
                val rvList = response.flatMap { (key, books) ->
                    listOf(ListItem.Section(key)) + books.map { ListItem.BookInfo(it) }
                }

                _uiState.value = AllBooksState.SectionsBooks(rvList)
            } else {
                _uiState.value = BaseUiState.Error("List is null")
            }

        }
    }

    private fun setFavoriteBook(id: Int) {
        val currentState = _uiState.value
        CoroutineScope(Dispatchers.IO).launch {
            if (currentState is AllBooksState.SectionsBooks) {

                val updatedRvBooksList = currentState.rvBooksList.map { item ->
                    when (item) {
                        is ListItem.BookInfo -> if (item.book.id == id) {
                            Log.d("mylog", "Updating book with id: $id")
                            item.copy(book = item.book.copy(isFavorite = !item.book.isFavorite))
                        } else {
                            item
                        }
                        else -> item
                    }
                }

                Log.d("mylog", "${currentState.rvBooksList}")
                Log.d("mylog", "$updatedRvBooksList")

//                _uiState.value = currentState.rvBooksList.map { it.copy() }
                Log.d("mylog", "State updated to AllBooksState.SectionsBooks")

                setFavoriteBookUC.setFavoriteBook(id)
            }
        }
    }
//
//    private fun fetchAllBooks() {
//        _uiState.value = BaseUiState.Loading
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = fetchBooksUseCase.fetchBooks()
//            if (response != null) {
//                _uiState.value = AllBooksState.GroupedBooks(response)
//            } else {
//                _uiState.value = BaseUiState.Error("List is null")
//            }
//
//        }
//    }

//    private fun borrowBook(id: Int) {
//        CoroutineScope(Dispatchers.Main).launch {
//            delay(Random.nextLong(100, 3000))
//
//            val currentState = _uiState.value
//            if (currentState is UiState.Data) {
//                _uiState.value = UiState.Data(
//                    currentState.data.toMutableList().also {
//                        val ix = currentState.data.indexOfFirst { book -> book.id == id }
//                        if (it[ix].availableCount > 0) {
//                            it[ix] = it[ix].copy(availableCount = it[ix].availableCount - 1)
////                            BorrowBookUseCase().borrowBook(it[ix].id)
//                        }
//                    })
//            }
//
//            borrowBookUseCase.borrowBook(id)
//        }
//    }


//        val currentState = _uiState.value
//        if (currentState is UiState.Data) {
//            _uiState.value = UiState.Data(
//                currentState.data.toMutableList().also {
//                    if (it[0].availableCount > 0) {
//                        it[0] = it[0].copy(availableCount = it[0].availableCount - 1)
//                    }
//                    LibraryDB.updateBook(it[0])
//                }
//            )
//        }


//    private fun returnBook(id: Int) {
//        CoroutineScope(Dispatchers.Main).launch {
//            delay(Random.nextLong(100, 3000))
//
//            val currentState = _uiState.value
//            if (currentState is UiState.Data) {
//                _uiState.value = UiState.Data(
//                    currentState.data.toMutableList().also {
//                        val ix = currentState.data.indexOfFirst { book -> book.id == id }
//                        it[ix] = it[ix].copy(availableCount = it[ix].availableCount + 1)
////                        ReturnBookUseCase().returnBook(it[ix].id)
//                    })
//            }
//
//            returnBookUseCase.returnBook(id)
//        }


//        val currentState = _uiState.value
//        if (currentState is UiState.Data) {
//            _uiState.value = UiState.Data(
//                currentState.data.toMutableList().also {
//                    it[0] = it[0].copy(availableCount = it[0].availableCount + 1)
//                }
//            )
////            currentState.data.forEachIndexed { index, book ->
////                if (index == 0) {
////                    book.borrowCount -= 1
//////                    _uiState.emit(UiState.Loading)
//////                    delay(150)
////                    _uiState.value = currentState.copy(data = currentState.data.toMutableList())
////                    return@forEachIndexed
////                }
////            }
//        }
//    }

}