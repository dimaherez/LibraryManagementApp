package com.example.librarymanagementapp.home.trending

import androidx.lifecycle.ViewModel
import com.example.domain.models.Book
import com.example.domain.use_cases.FetchTrendingAuthorsUC
import com.example.domain.use_cases.FetchTrendingBooksUC
import com.example.domain.use_cases.FetchTrendingGenresUC
import com.example.domain.use_cases.SetFavoriteBookUC
import com.example.librarymanagementapp.home.HomeBaseIntent
import com.example.librarymanagementapp.mvi.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingBooksViewModel @Inject constructor(
    private val fetchTrendingBooksUC: FetchTrendingBooksUC,
    private val fetchTrendingAuthorsUC: FetchTrendingAuthorsUC,
    private val fetchTrendingGenresUC: FetchTrendingGenresUC,
    private val setFavoriteBookUC: SetFavoriteBookUC
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchTrends()
    }

    fun processIntent(intent: HomeBaseIntent) {
        when (intent) {
            is TrendingBooksIntent.FetchTrends -> fetchTrends()
            is HomeBaseIntent.SetFavoriteBook -> setFavoriteBook(intent.id)
        }
    }

    private fun fetchTrends() {
        _uiState.value = UiState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val trendingBooks = fetchTrendingBooks()
            val trendingAuthors = fetchTrendingAuthors()
            val trendingGenres = fetchTrendingGenres()

            _uiState.value = UiState.Trending(trendingBooks, trendingAuthors, trendingGenres)
        }


    }

    private suspend fun fetchTrendingBooks(): List<Book> {
        return try {
            fetchTrendingBooksUC.fetchTrendingBooks()
        } catch (e: NullPointerException) {
            emptyList()
        }
    }

    private suspend fun fetchTrendingAuthors(): List<String>  {
        return try {
            fetchTrendingAuthorsUC.fetchTrendingAuthors()
        } catch (e: NullPointerException) {
            emptyList()
        }

    }
    private suspend fun fetchTrendingGenres(): List<String> {
        return try {
            fetchTrendingGenresUC.fetchTrendingGenres()
        } catch (e: NullPointerException) {
            emptyList()
        }
    }

    private fun setFavoriteBook(id: Int) {
        val currentState = _uiState.value
        if (currentState is UiState.Trending) {
            val ix = currentState.books.indexOfFirst { it.id == id }

            _uiState.value = currentState.copy(
                books = currentState.books.toMutableList().also { list ->
                        list[ix] = list[ix].copy(isFavorite = list[ix].isFavorite.not())
                    }
            )

            setFavoriteBookUC.setFavoriteBook(id)
        }
    }

}