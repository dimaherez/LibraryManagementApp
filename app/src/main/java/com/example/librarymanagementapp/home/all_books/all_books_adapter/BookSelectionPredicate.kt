package com.example.librarymanagementapp.home.all_books.all_books_adapter

import androidx.recyclerview.selection.SelectionTracker

class BookSelectionPredicate : SelectionTracker.SelectionPredicate<Long>() {
    override fun canSetStateForKey(key: Long, nextState: Boolean): Boolean {
        return key != ItemLookup.OUT_OF_CONTEXT_POSITION
    }

    override fun canSetStateAtPosition(position: Int, nextState: Boolean) = true
    override fun canSelectMultiple() = true
}