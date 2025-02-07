package com.example.librarymanagementapp.adapters

import android.widget.ImageButton
import com.example.librarymanagementapp.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Book

class BooksRvAdapter(private val onFavoriteCLick: (Int) -> Unit = {},
                     private val onInfoClick: (book: Book) -> Unit = {}) :
    RecyclerView.Adapter<BooksRvAdapter.BookViewHolder>() {

    private var books: List<Book> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Book>) {
        books = data
        notifyDataSetChanged()
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val authorTextView: TextView = itemView.findViewById(R.id.textViewAuthor)
        val borrowCount: TextView = itemView.findViewById(R.id.borrowCount)
        val favoriteBtn: ImageButton = itemView.findViewById(R.id.favoriteBtn)
        val infoBtn: ImageButton = itemView.findViewById(R.id.infoBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mvi_item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]// books[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        holder.borrowCount.text = "Available: ${book.availableCount}"

        if ( books[position].isFavorite) holder.favoriteBtn.setImageResource(R.drawable.baseline_star_24)
        else holder.favoriteBtn.setImageResource(R.drawable.baseline_star_border_24)

        holder.favoriteBtn.setOnClickListener {
            onFavoriteCLick(book.id)
        }

        holder.infoBtn.setOnClickListener {
            onInfoClick(book)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }

}
