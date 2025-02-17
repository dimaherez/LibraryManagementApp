package com.example.librarymanagementapp.home.trending

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
import com.example.librarymanagementapp.databinding.FragmentTrendingBinding
import com.example.librarymanagementapp.home.BooksRvAdapter
import com.example.librarymanagementapp.home.HomeBaseIntent
import com.example.librarymanagementapp.home.HomeFragmentDirections
import com.example.librarymanagementapp.mvi.BaseUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrendingBooksFragment : Fragment() {

    private lateinit var binding: FragmentTrendingBinding

    private val viewModel: TrendingBooksViewModel by viewModels()

    private val booksAdapter = BooksRvAdapter(
        onFavoriteCLick = { id -> toggleFavorite(id) },
        onInfoClick = { id -> navigateToInfoFragment(id) }
    )

    private val authorsAdapter = TrendingRvAdapter()
    private val genresAdapter = TrendingRvAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrendingBinding.inflate(layoutInflater)
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

        binding.rvTrendingBooks.adapter = booksAdapter
        binding.rvTrendingAuthors.adapter = authorsAdapter
        binding.rvTrendingGenres.adapter = genresAdapter
    }

    private fun handleState(state: BaseUiState) {
        when (state) {
            is BaseUiState.Loading -> {
                binding.booksLoader.visibility = View.VISIBLE
                binding.authorsLoader.visibility = View.VISIBLE
                binding.genresLoader.visibility = View.VISIBLE

                binding.rvTrendingBooks.isVisible = false
                binding.rvTrendingAuthors.isVisible = false
                binding.rvTrendingGenres.isVisible = false
            }

            is TrendingBooksState.Trending -> {
                binding.booksLoader.visibility = View.GONE
                binding.authorsLoader.visibility = View.GONE
                binding.genresLoader.visibility = View.GONE

                binding.rvTrendingBooks.isVisible = true
                binding.rvTrendingAuthors.isVisible = true
                binding.rvTrendingGenres.isVisible = true


                booksAdapter.setData(state.books)
                authorsAdapter.setData(state.authors)
                genresAdapter.setData(state.genres)
            }

            is BaseUiState.Error -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                binding.booksLoader.visibility = View.GONE
                binding.authorsLoader.visibility = View.GONE
                binding.genresLoader.visibility = View.GONE
            }
        }
    }

    private fun toggleFavorite(id: Int) {
        viewModel.processIntent(HomeBaseIntent.SetFavoriteBook(id))
    }

    private fun navigateToInfoFragment(bookId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToBookInfoFragment(bookId = bookId)
        findNavController().navigate(action)
    }

    private fun handleSwipe() {
        if (viewModel.uiState.value !is BaseUiState.Loading) {
            viewModel.processIntent(TrendingBooksIntent.FetchTrends)
        }
        binding.swipe.isRefreshing = false
    }
}