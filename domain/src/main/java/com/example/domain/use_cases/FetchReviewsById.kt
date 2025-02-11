package com.example.domain.use_cases

import com.example.domain.repository.LibraryRepo
import javax.inject.Inject

class FetchReviewsById @Inject constructor(private val libraryRepo: LibraryRepo) {
//    fun fetchReviewsById() = libraryRepo.fetchReviewsBy
}