package com.example.librarymanagementapp.home.all_books

import android.os.Bundle
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
import com.example.librarymanagementapp.databinding.FragmentAllBooksBinding
import com.example.librarymanagementapp.home.BooksRvAdapter
import com.example.librarymanagementapp.home.HomeBaseIntent
import com.example.librarymanagementapp.home.HomeFragmentDirections
import com.example.librarymanagementapp.mvi.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllBooksFragment : Fragment() {
    private lateinit var binding: FragmentAllBooksBinding
    private val viewModel by viewModels<AllBooksViewModel>()

    private val booksAdapter = BooksRvAdapter(
        onFavoriteCLick = { id -> toggleFavorite(id) },
        onInfoClick = { id -> navigateToInfoFragment(id) }
    )

    private fun toggleFavorite(id: Int) {
        viewModel.processIntent(HomeBaseIntent.SetFavoriteBook(id))
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

        binding.swipe.setOnRefreshListener {
            handleSwipe()
        }

        binding.rvAllBooks.adapter = booksAdapter
    }

    private fun handleState(state: UiState) {
        when (state) {
            is UiState.Loading -> {
                binding.progressLoader.visibility = View.VISIBLE
                binding.rvAllBooks.isVisible = false
            }

            is UiState.Books -> {
                booksAdapter.setData(state.books)
                binding.progressLoader.visibility = View.GONE
                binding.rvAllBooks.isVisible = true
            }

            is UiState.Error -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                binding.progressLoader.visibility = View.GONE
                binding.rvAllBooks.isVisible = false
            }

            else -> {}
        }
    }


    private fun handleSwipe() {
        if (viewModel.uiState.value !is UiState.Loading) {
            viewModel.processIntent(AllBooksIntent.FetchAllBooks)
        }
        binding.swipe.isRefreshing = false
    }
}