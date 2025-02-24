package com.example.librarymanagementapp.home.all_books.all_books_adapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToFavoriteCallback(private val adapter: SectionedBooksAdapter, private val selectionTracker: SelectionTracker<Long>) : ItemTouchHelper.Callback() {
    private val mPaint = Paint()

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        selectionTracker.clearSelection()
        if (direction == ItemTouchHelper.LEFT) {
            adapter.addToFavorites(position)
        } else if (direction == ItemTouchHelper.RIGHT) {
            adapter.removeFromFavorites(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            if (dX > 0) {
                // Swiping to the right
                mPaint.color = Color.RED

            } else if (dX < 0) {
                // Swiping to the left
                mPaint.color = Color.GREEN
            }

            c.drawRect(
                itemView.left.toFloat(),
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat(),
                mPaint
            )

        }
    }
}

