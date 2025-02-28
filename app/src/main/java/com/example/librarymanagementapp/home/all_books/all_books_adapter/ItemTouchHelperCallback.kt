package com.example.librarymanagementapp.home.all_books.all_books_adapter

interface ItemTouchHelperCallback {
    fun onSwipeToRight(position: Int)
    fun onSwipeToLeft(position: Int)
}