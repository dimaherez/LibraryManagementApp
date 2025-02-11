package com.example.domain.models

import java.time.LocalDateTime

data class Review(
    val author: String,
    val date: LocalDateTime = LocalDateTime.now(),
    val content: String,
    val rating: Int = 5
)
