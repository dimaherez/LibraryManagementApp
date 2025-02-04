package com.example.domain.models

import java.time.LocalDateTime

data class BookOrder(
    val book: com.example.domain.models.Book,
    val orderDateTime: LocalDateTime
)