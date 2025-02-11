package com.example.librarymanagementapp.home.favorite_books

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
import com.example.librarymanagementapp.databinding.FragmentFavoriteBooksBinding
import com.example.librarymanagementapp.home.BooksRvAdapter
import com.example.librarymanagementapp.home.HomeBaseIntent
import com.example.librarymanagementapp.home.HomeFragmentDirections
import com.example.librarymanagementapp.mvi.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteBooksFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBooksBinding
    private val viewModel: FavoriteBooksViewModel by viewModels()

    private val booksAdapter = BooksRvAdapter(
        onFavoriteCLick = { id -> toggleFavorite(id) },
        onInfoClick = { id -> navigateToInfoFragment(id) }
    )

    private val recommendationAdapter = RecommendedBooksRvAdapter(
        onInfoClick = { id -> navigateToInfoFragment(id) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBooksBinding.inflate(layoutInflater)
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

        binding.rvRecommendedBooks.adapter = recommendationAdapter
        binding.rvFavoriteBooks.adapter = booksAdapter
    }

    private fun handleState(state: UiState) {
        when (state) {
            is UiState.Loading -> {
                binding.progressLoader.visibility = View.VISIBLE
                binding.recommendedGroup.isVisible = false
                binding.favoriteGroup.isVisible = false
            }
            is UiState.FavoriteBooks -> {
                booksAdapter.setData(state.favoriteBooks)
                recommendationAdapter.setData(state.recommendations)

                binding.progressLoader.visibility = View.GONE
                binding.recommendedGroup.isVisible = true
                binding.favoriteGroup.isVisible = true
            }
            is UiState.Error -> {
                binding.progressLoader.visibility = View.GONE
                binding.recommendedGroup.isVisible = false
                binding.favoriteGroup.isVisible = false
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

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

    private fun handleSwipe() {
        if(viewModel.uiState.value !is UiState.Loading) {
            viewModel.processIntent(FavoriteBooksIntent.FetchData)
        }
        binding.swipe.isRefreshing = false
    }
}