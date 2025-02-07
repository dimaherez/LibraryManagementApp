package com.example.domain.models

import java.time.LocalDateTime

data class Review(
    val author: String,
    val date: LocalDateTime,
    val content: String
)
