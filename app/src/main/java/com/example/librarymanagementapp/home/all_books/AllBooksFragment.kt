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
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.librarymanagementapp.R
import com.example.librarymanagementapp.confirmation.AlertDialogConfirmation
import com.example.librarymanagementapp.databinding.FragmentAllBooksBinding
import com.example.librarymanagementapp.home.HomeBaseIntent
import com.example.librarymanagementapp.home.HomeFragmentDirections
import com.example.librarymanagementapp.home.all_books.all_books_adapter.BookSelectionPredicate
import com.example.librarymanagementapp.home.all_books.all_books_adapter.ItemLookup
import com.example.librarymanagementapp.home.all_books.all_books_adapter.ItemTouchHelperCallback
import com.example.librarymanagementapp.home.all_books.all_books_adapter.SectionedBooksAdapter
import com.example.librarymanagementapp.home.all_books.all_books_adapter.SwipeToFavoriteHelper
import com.example.librarymanagementapp.mvi.BaseUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AllBooksFragment : Fragment(), AlertDialogConfirmation.AlertDialogCallback,
    ItemTouchHelperCallback {
    private lateinit var binding: FragmentAllBooksBinding
    private val viewModel by viewModels<AllBooksViewModel>()
    private var tracker: SelectionTracker<Long>? = null
    private lateinit var itemTouchHelper: ItemTouchHelper

    private val booksAdapter = SectionedBooksAdapter(onFavoriteCLick = { id -> toggleFavorite(id) },
        onInfoClick = { id -> navigateToInfoFragment(id) })

    private fun toggleFavorite(id: Int) {
        viewModel.processIntent(HomeBaseIntent.ToggleFavoriteBook(id))
    }

    private fun navigateToInfoFragment(bookId: Int) {
        val navController = findNavController()
        if (navController.currentDestination?.id != R.id.bookInfoFragment) {
            navController.navigate(
                HomeFragmentDirections.actionHomeFragmentToBookInfoFragment(
                    bookId = bookId
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        setupListeners()
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.rvAllBooks.adapter = booksAdapter

        tracker = SelectionTracker.Builder(
            "selection-1",
            binding.rvAllBooks,
            StableIdKeyProvider(binding.rvAllBooks),
            ItemLookup(binding.rvAllBooks),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            BookSelectionPredicate()
        ).build()

        booksAdapter.tracker = tracker

        itemTouchHelper = ItemTouchHelper(
            SwipeToFavoriteHelper(
                requireContext(), itemTouchHelperCallback = this
            )
        )
        itemTouchHelper.attachToRecyclerView(binding.rvAllBooks)

        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                getSelectedItems().size.let {
                    binding.groupSelectionButtons.isVisible = it > 0
                }
            }
        })
    }

    private fun getSelectedItems() =
        tracker?.selection?.mapNotNull { booksAdapter.getItemByKey(it) } ?: emptyList()


    private fun setupListeners() {
        binding.swipe.setOnRefreshListener {
            handleSwipe()
        }

        binding.btnAddBook.setOnClickListener {
            viewModel.processIntent(AllBooksIntent.AddBook)
        }

        binding.btnSetAllFavorites.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionGlobalMyDialogFragment())
            getSelectedItems().forEach {
                viewModel.processIntent(AllBooksIntent.SetFavorite(it.book.id))
            }
            tracker?.clearSelection()
        }

        binding.btnRemoveAllFavorites.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionGlobalMyDialogFragment())
            val dialogFragment = AlertDialogConfirmation.newInstance(this)
            dialogFragment.show(parentFragmentManager, "AlertDialogConfirmation")
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
        tracker?.clearSelection()
    }

    override fun onPositiveClick() {
        getSelectedItems().forEach {
            viewModel.processIntent(AllBooksIntent.ResetFavorite(it.book.id))
        }
        tracker?.clearSelection()
    }

    override fun onNegativeClick() {
        tracker?.clearSelection()
    }

    override fun onSwipeToRight(position: Int) {
        viewModel.processIntent(
            AllBooksIntent.ResetFavorite(
                booksAdapter.getBookIdByPosition(
                    position
                )
            )
        )
        resetItemTouchHelper()
    }

    override fun onSwipeToLeft(position: Int) {
        viewModel.processIntent(
            AllBooksIntent.SetFavorite(
                booksAdapter.getBookIdByPosition(
                    position
                )
            )
        )
        resetItemTouchHelper()
    }

    private fun resetItemTouchHelper() { // The only workaround to revert swipe animation
        itemTouchHelper.attachToRecyclerView(null)
        itemTouchHelper.attachToRecyclerView(binding.rvAllBooks)
    }
}