package com.example.librarymanagementapp.home.favorite_books

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.domain.models.Book
import com.example.librarymanagementapp.R

class RecommendedBooksRvAdapter(
    private val onInfoClick: (bookId: Int) -> Unit = {}
) :
    RecyclerView.Adapter<RecommendedBooksRvAdapter.RecommendedBooksViewHolder>() {

    private lateinit var recommendList: List<Book>

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Book>) {
        recommendList = data
        notifyDataSetChanged()
    }

    class RecommendedBooksViewHolder(itemView: View) : ViewHolder(itemView) {
        val bookTitle: TextView = itemView.findViewById(R.id.recommendedBookTitle)
        val bookAuthor: TextView = itemView.findViewById(R.id.recommendedBookAuthor)
        val infoBtn: ImageButton = itemView.findViewById(R.id.infoBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedBooksViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recommend, parent, false)
        return RecommendedBooksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recommendList.size
    }

    override fun onBindViewHolder(holder: RecommendedBooksViewHolder, position: Int) {
        val book = recommendList[position]
        holder.bookTitle.text = book.title
        holder.bookAuthor.text = book.author

        holder.infoBtn.setOnClickListener{
            onInfoClick(book.id)
        }
    }
}