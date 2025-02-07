package com.example.domain.models

import android.os.Parcelable
import com.example.domain.enums.Genre
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Book(
    val id: Int,
    val title: String,
    val genre: Genre,
    val author: String,
    val releaseDate: LocalDate,
    val price: Float,
    val rating: Int = 5,
    val description: String = "",
    var isAvailable: Boolean = false,
    var borrowCount: Int = 0,
    var availableCount: Int = 0,
    var isFavorite: Boolean = false,
) : Parcelable






