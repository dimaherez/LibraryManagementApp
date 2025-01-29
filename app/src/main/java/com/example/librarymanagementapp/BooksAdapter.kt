package com.example.librarymanagementapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagementapp.models.Book

class BooksAdapter(private val itemClickListener: (Book) -> Unit = {}) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    private var books: List<Book> = emptyList()
    private var sortedBooks: List<Book> = books
    var sortedType = 0

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Book>) {
        books = data
        sortedBooks = when (sortedType) {
            1 -> {
                emptyList()
            }
            2 -> {
                emptyList()
            }
            else -> {
                emptyList()
            }
        }
        notifyDataSetChanged()
    }

    fun sort(type: Int) {
        sortedType = type
        setData(books)
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val authorTextView: TextView = itemView.findViewById(R.id.textViewAuthor)
        val borrowCount: TextView = itemView.findViewById(R.id.borrowCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        holder.borrowCount.text = book.borrowCount.toString()

        holder.itemView.setOnClickListener {
            itemClickListener(book)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }
}
