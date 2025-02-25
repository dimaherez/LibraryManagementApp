package com.example.librarymanagementapp.home.all_books

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.librarymanagementapp.databinding.FragmentAllBooksBinding
import com.example.librarymanagementapp.home.HomeBaseIntent
import com.example.librarymanagementapp.home.HomeFragmentDirections
import com.example.librarymanagementapp.home.all_books.all_books_adapter.BookSelectionPredicate
import com.example.librarymanagementapp.home.all_books.all_books_adapter.ItemLookup
import com.example.librarymanagementapp.home.all_books.all_books_adapter.MyItemKeyProvider
import com.example.librarymanagementapp.home.all_books.all_books_adapter.SectionedBooksAdapter
import com.example.librarymanagementapp.home.all_books.all_books_adapter.SwipeToFavoriteCallback
import com.example.librarymanagementapp.mvi.BaseUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AllBooksFragment : Fragment() {
    private lateinit var binding: FragmentAllBooksBinding
    private val viewModel by viewModels<AllBooksViewModel>()
    private var tracker: SelectionTracker<Long>? = null
    private lateinit var selectedListItems: List<ListItem.BookInfo>

    private val booksAdapter = SectionedBooksAdapter(
        onFavoriteCLick = { id -> toggleFavorite(id) },
        onInfoClick = { id -> navigateToInfoFragment(id) }
    )

    private fun toggleFavorite(id: Int) {
        viewModel.processIntent(HomeBaseIntent.ToggleFavoriteBook(id))
    }

    private fun navigateToInfoFragment(bookId: Int) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToBookInfoFragment(
                bookId = bookId
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllBooksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleState(state)
                }
            }
        }

//        viewModel.uiState.observe(viewLifecycleOwner) { state ->
//            handleState(state)
//        }

        setupListeners()
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.rvAllBooks.adapter = booksAdapter

        tracker = SelectionTracker.Builder(
            "selection-1",
            binding.rvAllBooks,
            MyItemKeyProvider(booksAdapter),
            ItemLookup(binding.rvAllBooks),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            BookSelectionPredicate()
        ).build()

        booksAdapter.tracker = tracker
        ItemTouchHelper(SwipeToFavoriteCallback(booksAdapter, tracker!!)).attachToRecyclerView(binding.rvAllBooks)

        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    selectedListItems =
                        tracker?.selection?.map { booksAdapter.getItemByKey(it)!! } ?: emptyList()

                    Log.d(
                        "mylog",
                        "Selected items: ${selectedListItems.joinToString { it.book.title }}"
                    )

                    tracker?.selection?.size()?.let {
                        binding.groupSelectionButtons.isVisible = it > 0
                    }
                }
            })
    }

    private fun setupListeners() {
        binding.swipe.setOnRefreshListener {
            handleSwipe()
        }

        binding.btnAddBook.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFragmentAddBook())
            viewModel.processIntent(AllBooksIntent.AddBook)
        }

        binding.btnSetAllFavorites.setOnClickListener {
            selectedListItems.forEach {
                viewModel.processIntent(AllBooksIntent.SetFavorite(it.book.id))
            }
            tracker?.clearSelection()
        }

        binding.btnRemoveAllFavorites.setOnClickListener {
            selectedListItems.forEach {
                viewModel.processIntent(AllBooksIntent.ResetFavorite(it.book.id))
            }
            tracker?.clearSelection()
        }
    }

    private fun handleState(state: BaseUiState) {
        when (state) {
            is BaseUiState.Loading -> {
                binding.progressLoader.visibility = View.VISIBLE
                binding.rvAllBooks.isVisible = false
            }

            is AllBooksState.AllBooks -> {
                booksAdapter.setData(state.books)
                binding.progressLoader.visibility = View.GONE
                binding.rvAllBooks.isVisible = true
            }

            is BaseUiState.Error -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                binding.progressLoader.visibility = View.GONE
                binding.rvAllBooks.isVisible = false
            }
        }
    }

    private fun handleSwipe() {
        if (viewModel.uiState.value !is BaseUiState.Loading) {
            viewModel.processIntent(AllBooksIntent.FetchAllBooks)
        }
        binding.swipe.isRefreshing = false
    }

}