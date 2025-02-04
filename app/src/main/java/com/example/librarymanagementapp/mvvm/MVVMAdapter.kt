package com.example.librarymanagementapp.mvvm

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagementapp.R
import com.example.domain.models.Book

class MVVMAdapter(private val itemClickListener: (com.example.domain.models.Book) -> Unit = {}) :
    RecyclerView.Adapter<MVVMAdapter.BookViewHolder>() {

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]// books[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        holder.borrowCount.text = "Available: ${book.availableCount}"

        holder.itemView.setOnClickListener {
            itemClickListener(book)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }


}
