package com.example.librarymanagementapp.home.all_books

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.domain.models.Book
import com.example.librarymanagementapp.R
import com.example.librarymanagementapp.databinding.ItemBookBinding
import com.example.librarymanagementapp.databinding.ItemSectionBinding

private const val ITEM_VIEW_TYPE_SECTION = 0
private const val ITEM_VIEW_TYPE_BOOK = 1

class GroupedBooksAdapter(
    private val onFavoriteCLick: (Int) -> Unit = {},
    private val onInfoClick: (bookId: Int) -> Unit = {}
) : RecyclerView.Adapter<ViewHolder>() {

    private var itemsList: List<ListItem> = listOf()
    lateinit var context: Context

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ListItem>) {
        Log.d("mylog", "Set Data")
        itemsList = data

        notifyDataSetChanged()
    }


    class SectionViewHolder(private val binding: ItemSectionBinding) :
        ViewHolder(binding.root) {
        fun bind(section: Char) {
            binding.sectionTitle.text = section.toString()
        }
    }

    class BookViewHolder(
        private val binding: ItemBookBinding,
        private val parent: ViewGroup,
        private val onFavoriteCLick: (Int) -> Unit = {},
        private val onInfoClick: (bookId: Int) -> Unit = {}
    ) :
        ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.textViewTitle.text = book.title
            binding.textViewAuthor.text = book.author

            binding.availableCount.text = parent.context.resources.getQuantityString(
                R.plurals.plural_book_available, book.availableCount, book.availableCount
            )

            binding.borrowCount.text = parent.context.resources.getQuantityString(
                R.plurals.plural_order, book.borrowCount, book.borrowCount
            )

            binding.reviewsCount.text = parent.context.resources.getQuantityString(
                R.plurals.plural_review, book.reviews.size, book.reviews.size
            )

            binding.bookItemRatingBar.rating = book.rating.toFloat()

            if (book.isFavorite) binding.favoriteBtn.setImageResource(R.drawable.baseline_star_24)
            else binding.favoriteBtn.setImageResource(R.drawable.baseline_star_border_24)

            binding.favoriteBtn.setOnClickListener {
                onFavoriteCLick(book.id)
            }

            binding.infoBtn.setOnClickListener {
                onInfoClick(book.id)
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (itemsList[position]) {
            is ListItem.Section -> ITEM_VIEW_TYPE_SECTION
            is ListItem.BookInfo -> ITEM_VIEW_TYPE_BOOK
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return when (viewType) {
            ITEM_VIEW_TYPE_SECTION -> {
                val binding =
                    ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SectionViewHolder(binding)
            }

            ITEM_VIEW_TYPE_BOOK -> {
                val binding =
                    ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BookViewHolder(
                    binding,
                    parent,
                    onFavoriteCLick = onFavoriteCLick,
                    onInfoClick = onInfoClick
                )
            }

            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SectionViewHolder -> {
                holder.bind((itemsList[position] as ListItem.Section).letter)
            }

            is BookViewHolder -> {
                holder.bind((itemsList[position] as ListItem.BookInfo).book)
            }
        }
    }

    override fun getItemCount(): Int = itemsList.size
}