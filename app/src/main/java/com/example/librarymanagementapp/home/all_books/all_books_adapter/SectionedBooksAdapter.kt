package com.example.librarymanagementapp.home.all_books.all_books_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.domain.models.Book
import com.example.librarymanagementapp.R
import com.example.librarymanagementapp.databinding.ItemBookBinding
import com.example.librarymanagementapp.databinding.ItemSectionBinding


private const val ITEM_VIEW_TYPE_SECTION = 0
const val ITEM_VIEW_TYPE_BOOK = 1

class SectionedBooksAdapter(
    private val onFavoriteCLick: (Int) -> Unit = {},
    private val onInfoClick: (bookId: Int) -> Unit = {}
) : RecyclerView.Adapter<ViewHolder>() {

    lateinit var context: Context
    private var itemsList: List<ListItem> = listOf()
    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return when (val item = itemsList[position]) {
            is ListItem.BookInfo -> item.book.id.toLong()
            is ListItem.Section -> item.letter.code.toLong()
        }
    }

    fun getItemByKey(key: Long): ListItem.BookInfo? {
        return itemsList.filterIsInstance<ListItem.BookInfo>()
            .find { it.book.id.toLong() == key }
    }

    fun getPosition(key: Long): Int {
        return itemsList.indexOfFirst {
            when (it) {
                is ListItem.BookInfo -> it.book.id.toLong() == key
                is ListItem.Section -> it.letter.code.toLong() == key
            }
        }
    }

    fun getBookIdByPosition(position: Int): Int {
        return (itemsList[position] as ListItem.BookInfo).book.id
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Book>) {

        itemsList = data
            .sortedBy { it.title }
            .groupBy { it.title.first().uppercaseChar() }
            .toSortedMap()
            .flatMap { (key, books) ->
                listOf(ListItem.Section(key)) + books.map { ListItem.BookInfo(it) }
            }

        notifyDataSetChanged()
    }

    private fun getPositionsForInitial(initial: Char): List<Int> {
        val positions: MutableList<Int> = ArrayList()
        itemsList.forEachIndexed { index, item ->
            if (item is ListItem.BookInfo) {
                if (item.book.title.first().equals(initial, ignoreCase = true)) {
                    positions.add(index)
                }
            }
        }
        return positions
    }

    private fun selectItemsForInitial(initial: Char) {
        getPositionsForInitial(initial).forEach {
            tracker?.select(getItemId(it))
        }
    }

    private fun deselectItemsForInitial(initial: Char) {
        getPositionsForInitial(initial).forEach {
            tracker?.deselect(getItemId(it))
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

    class SectionViewHolder(private val binding: ItemSectionBinding) :
        ViewHolder(binding.root) {

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> {
            return object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Long = itemId
            }
        }

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

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Long = itemId
            }

        fun bind(book: Book, isSelected: Boolean) {
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

            binding.constraintLayout.isActivated = isSelected
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SectionViewHolder -> {
                val initial = (itemsList[position] as ListItem.Section).letter

                if (tracker?.isSelected(getItemId(position)) == true) {
                    selectItemsForInitial(initial)
                } else {
                    deselectItemsForInitial(initial)
                }

                holder.bind(initial)
            }

            is BookViewHolder -> {
                holder.bind(
                    (itemsList[position] as ListItem.BookInfo).book,
                    tracker?.isSelected(getItemId(position)) ?: false
                )
            }
        }
    }

    override fun getItemCount(): Int = itemsList.size
}