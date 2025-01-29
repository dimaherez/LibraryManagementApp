package com.example.librarymanagementapp.models

import java.time.LocalDateTime

data class BookOrder(
    val book: Book,
    val orderDateTime: LocalDateTime
)