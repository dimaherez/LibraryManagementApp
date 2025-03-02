package com.example.librarymanagementapp.home.all_books.all_books_adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class ItemLookup(private val rv: RecyclerView) : ItemDetailsLookup<Long>() {
    private val outOfContextSelection = object : ItemDetails<Long>() {
        override fun getPosition(): Int = OUT_OF_CONTEXT_POSITION.toInt()
        override fun getSelectionKey() = OUT_OF_CONTEXT_POSITION
    }

    override fun getItemDetails(event: MotionEvent): ItemDetails<Long> {
        val view = rv.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return when (val rvChildViewHolder = rv.getChildViewHolder(view)) {
                is SectionedBooksAdapter.BookViewHolder -> {
                    rvChildViewHolder.getItemDetails()
                }

                else -> {
                    outOfContextSelection
                }
            }
        }
        return outOfContextSelection
    }

    companion object {
        const val OUT_OF_CONTEXT_POSITION = 10000L
    }
}