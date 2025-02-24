package com.example.librarymanagementapp.home.all_books.all_books_adapter

import androidx.recyclerview.selection.ItemKeyProvider

class MyItemKeyProvider(private val adapter: SectionedBooksAdapter) : ItemKeyProvider<Long>(SCOPE_MAPPED) {
    override fun getKey(position: Int): Long {
        return adapter.getItemId(position)
    }

    override fun getPosition(key: Long): Int {
        return adapter.itemsList.indexOfFirst { it.hashCode().toLong() == key }
    }
}
