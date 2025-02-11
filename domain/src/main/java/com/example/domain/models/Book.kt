package com.example.domain.models

import com.example.domain.enums.Genre
import java.time.LocalDate

data class Book(
    val id: Int,
    val title: String,
    val genre: Genre,
    val author: String,
    val releaseDate: LocalDate,
    val price: Float,
    val reviews: List<Review> = emptyList(),
    val description: String = "",
    var isAvailable: Boolean = false,
    var borrowCount: Int = 0,
    var availableCount: Int = 0,
    var isFavorite: Boolean = false,
) {
    val rating = reviews.map { it.rating }.average().toInt()
}






