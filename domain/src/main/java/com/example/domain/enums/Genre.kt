package com.example.domain.enums

enum class Genre {
    FICTION,
    FANTASY,
    SCIENCE_FICTION,
    MYSTERY,
    BIOGRAPHY,
}

fun Genre.string() =
    when(this) {
        Genre.FICTION -> "Fiction"
        Genre.FANTASY -> "Fantasy"
        Genre.SCIENCE_FICTION -> "Science Fiction"
        Genre.MYSTERY -> "Mystery"
        Genre.BIOGRAPHY -> "Biography"
    }


