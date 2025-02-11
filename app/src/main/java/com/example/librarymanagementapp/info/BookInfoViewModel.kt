package com.example.librarymanagementapp.info

import androidx.lifecycle.ViewModel
import com.example.domain.use_cases.FetchBookByIdUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookInfoViewModel @Inject constructor (
    private val fetchBookByIdUC: FetchBookByIdUC
) : ViewModel() {


    fun fetchBookById(bookId: Int) = fetchBookByIdUC.fetchBookById(bookId)
}