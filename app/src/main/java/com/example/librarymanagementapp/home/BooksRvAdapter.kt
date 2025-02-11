package com.example.librarymanagementapp.home

import android.widget.ImageButton
import com.example.librarymanagementapp.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Book

class BooksRvAdapter(private val onFavoriteCLick: (Int) -> Unit = {},
                     private val onInfoClick: (bookId: Int) -> Unit = {}) :
    RecyclerView.Adapter<BooksRvAdapter.BookViewHolder>() {

    private var books: List<Book> = emptyList()
    lateinit var context: Context

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Book>) {
        books = data
        notifyDataSetChanged()
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val authorTextView: TextView = itemView.findViewById(R.id.textViewAuthor)
        val availableCount: TextView = itemView.findViewById(R.id.availableCount)
        val favoriteBtn: ImageButton = itemView.findViewById(R.id.favoriteBtn)
        val infoBtn: ImageButton = itemView.findViewById(R.id.infoBtn)
        val borrowCount: TextView = itemView.findViewById(R.id.borrowCount)
        val reviewCount: TextView = itemView.findViewById(R.id.reviewsCount)
        val ratingBar: RatingBar = itemView.findViewById(R.id.bookItemRatingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        context = parent.context
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]// books[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
//        holder.availableCount.text = "Available: ${book.availableCount}"

        holder.availableCount.text = context.resources.getQuantityString(
            R.plurals.plural_book_available, book.availableCount, book.availableCount)

        holder.borrowCount.text = context.resources.getQuantityString(
            R.plurals.plural_order, book.borrowCount, book.borrowCount)

        holder.reviewCount.text = context.resources.getQuantityString(
            R.plurals.plural_review, book.reviews.size, book.reviews.size)

        holder.ratingBar.rating = book.rating.toFloat()

        if ( books[position].isFavorite) holder.favoriteBtn.setImageResource(R.drawable.baseline_star_24)
        else holder.favoriteBtn.setImageResource(R.drawable.baseline_star_border_24)

        holder.favoriteBtn.setOnClickListener {
            onFavoriteCLick(book.id)
        }

        holder.infoBtn.setOnClickListener {
            onInfoClick(book.id)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }

}
