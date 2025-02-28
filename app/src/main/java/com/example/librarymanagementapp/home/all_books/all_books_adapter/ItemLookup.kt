package com.example.librarymanagementapp.home.all_books.all_books_adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class ItemLookup(private val rv: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = rv.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return when (val rvChildViewHolder = rv.getChildViewHolder(view)) {
                is SectionedBooksAdapter.BookViewHolder -> {
                    rvChildViewHolder.getItemDetails()
                }
                is SectionedBooksAdapter.SectionViewHolder -> {
                    rvChildViewHolder.getItemDetails()
                }
                else -> {
                    null
                }
            }
        }
        return null
    }
}