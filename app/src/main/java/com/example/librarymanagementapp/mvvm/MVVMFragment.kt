package com.example.librarymanagementapp.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librarymanagementapp.databinding.FragmentMvvmBinding
import com.example.librarymanagementapp.models.Book
import com.example.librarymanagementapp.UiState
import com.example.librarymanagementapp.BooksAdapter
import kotlinx.coroutines.launch


class MVVMFragment : Fragment() {
    private lateinit var binding: FragmentMvvmBinding
    private val viewModel by viewModels<ViewModelMVVM>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMvvmBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchBooks()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleState(state)
                }
            }
        }

        binding.sortBtn.setOnClickListener {
            viewModel.sortBooks()
        }

        binding.filterBtn.setOnClickListener {
            viewModel.filterBooks()
        }
    }

    private fun handleState(state: UiState) {
        when (state) {
            is UiState.Loading -> {
                binding.loadingTv.visibility = View.VISIBLE
            }
            is UiState.Data -> {
                displayResult(state.data)
                binding.loadingTv.visibility = View.GONE
                binding.sortBtn.isEnabled = true
                binding.filterBtn.isEnabled = true
            }
            is UiState.Error -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                binding.loadingTv.visibility = View.GONE
            }
        }
    }

    private fun displayResult(books: List<Book>) {
        val recyclerView = binding.recyclerViewBooks
        recyclerView.layoutManager = LinearLayoutManager(context)
        val booksAdapter = BooksAdapter(books = books)
        recyclerView.adapter = booksAdapter
    }
}