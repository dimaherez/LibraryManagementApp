package com.example.librarymanagementapp.reviews

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Review
import com.example.librarymanagementapp.R
import java.time.temporal.ChronoUnit

class ReviewsRvAdapter:
    RecyclerView.Adapter<ReviewsRvAdapter.ReviewsHolder>() {

    private var reviews: List<Review> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Review>) {
        reviews = data

        Log.d("mylog", "ReviewsAdatper ${reviews}")
        notifyDataSetChanged()
    }

    class ReviewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewAuthor: TextView = itemView.findViewById(R.id.reviewAuthorTv)
        val reviewContent: TextView = itemView.findViewById(R.id.reviewContentTv)
        val publicationDate: TextView = itemView.findViewById(R.id.publicationDateTv)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewsHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewsHolder, position: Int) {
        val review = reviews[position]
        holder.reviewAuthor.text = review.author
        holder.reviewContent.text = review.content
        holder.publicationDate.text = review.date.truncatedTo(ChronoUnit.SECONDS).toString()
        holder.ratingBar.rating = review.rating.toFloat()
    }

    override fun getItemCount(): Int {
        return reviews.size
    }
}