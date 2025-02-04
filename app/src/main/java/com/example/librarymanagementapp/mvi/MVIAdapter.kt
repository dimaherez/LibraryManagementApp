package com.example.librarymanagementapp.mvi

import android.widget.ImageButton
import com.example.librarymanagementapp.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Book

class MVIAdapter(private val onBorrowClick: (Int) -> Unit = {},
                 private val onReturnClick: (Int) -> Unit = {}) :
    RecyclerView.Adapter<MVIAdapter.BookViewHolder>() {

    private var books: List<com.example.domain.models.Book> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<com.example.domain.models.Book>) {
        books = data
        notifyDataSetChanged()
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val authorTextView: TextView = itemView.findViewById(R.id.textViewAuthor)
        val borrowCount: TextView = itemView.findViewById(R.id.borrowCount)
        val borrowButton: ImageButton = itemView.findViewById(R.id.borrowButton)
        val returnButton: ImageButton = itemView.findViewById(R.id.returnButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mvi_item_book, parent, false)
        return BookViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]// books[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        holder.borrowCount.text = "Available: ${book.availableCount}"

        holder.borrowButton.setOnClickListener {
            onBorrowClick(book.id)
            notifyDataSetChanged()
        }

        holder.returnButton.setOnClickListener {
            onReturnClick(book.id)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }


}
