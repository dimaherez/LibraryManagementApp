package com.example.librarymanagementapp.mvp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagementapp.R
import com.example.domain.models.Book

class MVPAdapter(private val itemClickListener: (com.example.domain.models.Book) -> Unit = {}) :
    RecyclerView.Adapter<MVPAdapter.BookViewHolder>() {

    private var books: List<com.example.domain.models.Book> = emptyList()
    private var initBooks: List<com.example.domain.models.Book> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<com.example.domain.models.Book>) {
        initBooks = data
        books = data
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterBooks(query: String?) {
        if (query.isNullOrEmpty()) {
            books = initBooks
            notifyDataSetChanged()
            return
        }
        books = initBooks.filter {
            it.title.lowercase().contains(query.lowercase()) ||
                    it.author.lowercase().contains(query.lowercase())
        }
        notifyDataSetChanged()
    }


    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val authorTextView: TextView = itemView.findViewById(R.id.textViewAuthor)
        val borrowCount: TextView = itemView.findViewById(R.id.borrowCount)
        val favoriteBtn: ImageButton = itemView.findViewById(R.id.favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mvp_item_book, parent, false)
        return BookViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]// books[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        holder.borrowCount.text = "Available: ${book.availableCount}"

        if ( books[position].isFavorite) holder.favoriteBtn.setImageResource(R.drawable.baseline_star_24)
        else holder.favoriteBtn.setImageResource(R.drawable.baseline_star_border_24)

        holder.favoriteBtn.setOnClickListener {
            books[position].isFavorite = !books[position].isFavorite

            itemClickListener( books[position])

            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }


}
