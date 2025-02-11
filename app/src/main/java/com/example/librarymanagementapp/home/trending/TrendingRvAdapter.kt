package com.example.librarymanagementapp.home.trending

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.librarymanagementapp.R

class TrendingRvAdapter: RecyclerView.Adapter<TrendingRvAdapter.TrendingViewHolder>() {

    private lateinit var trendingList: List<String>

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<String>) {
        trendingList = data
        notifyDataSetChanged()
    }

    class TrendingViewHolder(itemView: View): ViewHolder(itemView) {
        val data: TextView = itemView.findViewById(R.id.trendName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trending, parent, false)
        return TrendingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trendingList.size
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        holder.data.text = trendingList[position]
    }
}