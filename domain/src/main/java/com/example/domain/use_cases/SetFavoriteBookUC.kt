package com.example.domain.use_cases

import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class SetFavoriteBookUC @Inject constructor(private val libraryRepo: LibraryRepo) {
    fun setFavoriteBook(id: Int) {
        libraryRepo.setFavoriteBook(id)
    }
}