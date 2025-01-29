package com.example.librarymanagementapp.models

import com.example.librarymanagementapp.enums.Genre
import java.time.LocalDate

data class Book(
    val id: Int,
    val title: String,
    val genre: Genre,
    val author: String,
    val releaseDate: LocalDate,
    val price: Float,
    var isAvailable: Boolean = false,
    var borrowCount: Int = 0,
    var availableCount: Int = 0
)



