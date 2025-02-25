package com.example.librarymanagementapp.home.favorite_books

import androidx.lifecycle.ViewModel
import com.example.domain.models.Book
import com.example.domain.use_cases.BooksRecommendationUC
import com.example.domain.use_cases.FetchFavoriteBooksUC
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
class FavoriteBooksViewModel @Inject constructor(
    private val fetchFavoriteBooksUC: FetchFavoriteBooksUC,
    private val fetchBooksRecommendationUC: BooksRecommendationUC,
    private val setFavoriteBookUC: SetFavoriteBookUC
) : ViewModel() {
    private val _uiState = MutableStateFlow<BaseUiState>(BaseUiState.Loading)
    val uiState: StateFlow<BaseUiState> = _uiState

    init {
        fetchData()
    }

    fun processIntent(intent: HomeBaseIntent) {
        when (intent) {
            is FavoriteBooksIntent.FetchData -> fetchData()
            is HomeBaseIntent.ToggleFavoriteBook -> setFavoriteBook(intent.id)
        }
    }

    private fun fetchData() {
        _uiState.value = BaseUiState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteBooks = fetchFavoriteBooks()
            val recommendedBooks = fetchBooksRecommendation()

            _uiState.value = FavoriteBooksState.FavoriteBooks(favoriteBooks, recommendedBooks)
        }
    }


    private suspend fun fetchFavoriteBooks(): List<Book> {
        return try{
            fetchFavoriteBooksUC.fetchFavoriteBooks()
        } catch (e: NullPointerException){
            _uiState.value = BaseUiState.Error("List is null")
            emptyList()

        }
    }

    private suspend fun fetchBooksRecommendation(): List<Book> {
        return try{
            fetchBooksRecommendationUC.fetchBooksRecommendation()
        } catch (e: NullPointerException){
            _uiState.value = BaseUiState.Error("List is null")
            emptyList()

        }
    }

    private fun setFavoriteBook(id: Int) {
        val currentState = _uiState.value
        CoroutineScope(Dispatchers.Main).launch {
            if (currentState is FavoriteBooksState.FavoriteBooks) {
                val ix = currentState.favoriteBooks.indexOfFirst { it.id == id }

                _uiState.value = currentState.copy(
                    favoriteBooks = currentState.favoriteBooks.toMutableList()
                        .also { list -> list[ix] = list[ix].copy(isFavorite = list[ix].isFavorite.not()) }
                )

                setFavoriteBookUC.toggleFavorite(id)
            }
        }
    }
}