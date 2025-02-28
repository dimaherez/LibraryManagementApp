package com.example.librarymanagementapp.home.all_books.all_books_adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagementapp.R

class SwipeToFavoriteHelper(
    context: Context,
    private val itemTouchHelperCallback: ItemTouchHelperCallback
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val setFavoriteIcon =
        ContextCompat.getDrawable(context, R.drawable.baseline_star_24)!!
    private val resetFavoriteIcon =
        ContextCompat.getDrawable(context, R.drawable.baseline_star_border_24)!!
    private val intrinsicWidth = setFavoriteIcon.intrinsicWidth
    private val intrinsicHeight = setFavoriteIcon.intrinsicHeight
    private val backgroundColor = ContextCompat.getColor(context, R.color.item_swipe_background)
    private val background = ColorDrawable()
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder is SectionedBooksAdapter.BookViewHolder) {
            val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            return makeMovementFlags(0, swipeFlags)
        } else {
            return makeMovementFlags(0, 0)
        }
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
        if (direction == ItemTouchHelper.LEFT) {
            itemTouchHelperCallback.onSwipeToLeft(position)
        } else if (direction == ItemTouchHelper.RIGHT) {
            itemTouchHelperCallback.onSwipeToRight(position)
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
        val itemView = viewHolder.itemView
        if (dX == 0f && !isCurrentlyActive) {
            clearCanvas(c, itemView)
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            drawBackground(c, itemView, dX)
            drawIcon(c, itemView, dX)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX / 3, dY / 3, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, itemView: View) {
        c?.drawRect(itemView.right.toFloat(), itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat(), clearPaint)
    }

    private fun drawBackground(c: Canvas, itemView: View, dX: Float) {
        background.color = backgroundColor
        val backgroundBounds = if (dX > 0) {
            Rect(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
        } else {
            Rect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        }
        background.bounds = backgroundBounds
        background.draw(c)
    }

    private fun drawIcon(c: Canvas, itemView: View, dX: Float) {
        val icon = if (dX > 0) resetFavoriteIcon else setFavoriteIcon
        val itemHeight = itemView.bottom - itemView.top
        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconTop = itemView.top + iconMargin
        val iconLeft = if (dX > 0) itemView.left + iconMargin else itemView.right - iconMargin - intrinsicWidth
        val iconRight = iconLeft + intrinsicWidth
        val iconBottom = iconTop + intrinsicHeight
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        icon.draw(c)
    }

}

