package com.example.librarymanagementapp.books

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
import com.example.domain.models.Book
import com.example.librarymanagementapp.NavGraphDirections
import com.example.librarymanagementapp.adapters.BooksRvAdapter
import com.example.librarymanagementapp.databinding.FragmentBooksBinding
import com.example.librarymanagementapp.mvi.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BooksFragment : Fragment() {
    private lateinit var binding: FragmentBooksBinding
    private val viewModel by viewModels<BooksViewModel>()

    private val booksAdapter = BooksRvAdapter(
        onFavoriteCLick = { id -> toggleFavorite(id) },
        onInfoClick = { book -> navigateToInfoFragment(book) }
    )

    private fun toggleFavorite(id: Int) {
        viewModel.processIntent(BooksIntent.SetFavoriteBook(id))
    }

    private fun navigateToInfoFragment(book: Book) {
        val action = BooksFragmentDirections.actionBooksFragmentToBookInfoFragment(book = book)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBooksBinding.inflate(layoutInflater)
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

        binding.btnAlertDialog.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalMyDialogFragment())
        }

        binding.recyclerViewBooks.adapter = booksAdapter
    }

    private fun handleState(state: UiState) {
        when (state) {
            is UiState.Loading -> {
                binding.progressLoader.visibility = View.VISIBLE
                binding.recyclerViewBooks.isVisible = false
            }
            is UiState.Data -> {
                booksAdapter.setData(state.data)
                binding.progressLoader.visibility = View.GONE
                binding.recyclerViewBooks.isVisible = true
            }
            is UiState.Error -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                binding.progressLoader.visibility = View.GONE
                binding.recyclerViewBooks.isVisible = false
            }
        }
    }



    private fun handleSwipe() {
        if(viewModel.uiState.value !is UiState.Loading) {
            viewModel.fetchBooks()
        }
        binding.swipe.isRefreshing = false
    }
}