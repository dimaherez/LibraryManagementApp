package com.example.librarymanagementapp.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.librarymanagementapp.databinding.FragmentMvvmBinding
import com.example.librarymanagementapp.models.Book
import com.example.librarymanagementapp.enums.Genre
import java.time.LocalDate
import kotlin.random.Random


class MVVMFragment : Fragment() {
    private lateinit var binding: FragmentMvvmBinding
    private val viewModel by viewModels<ViewModelMVVM>()
    private val booksAdapter = MVVMAdapter()

    private var testCounter = 0 // Counter for test titles

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMvvmBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.data.observe(viewLifecycleOwner) { books ->
            processBooks(books)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }

        viewModel.createLoading.observe(viewLifecycleOwner) { isLoading ->
            setCreateLoading(isLoading)
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            showError(message)
        }

        binding.sortBtn.setOnClickListener {
            viewModel.sortBooks()
        }

        binding.filterBtn.setOnClickListener {
            viewModel.filterBooks()
        }

        binding.createBtn.setOnClickListener {
            createBook()
        }

        binding.swipe.setOnRefreshListener {
            handleSwipe()
        }

        binding.recyclerViewBooks.adapter = booksAdapter
    }

    private fun createBook() {
        viewModel.createBook(
            Book(
                id = Random.nextInt(1000, 1999),
                title = "TEST BOOK ${testCounter++}",
                genre = Genre.FICTION,
                author = "Author",
                releaseDate = LocalDate.of(1949, 6, 8),
                price = 14.99f,
                isAvailable = true,
                borrowCount = 0,
                availableCount = 5
            )
        )
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressLoader.visibility = View.VISIBLE
            binding.recyclerViewBooks.isVisible = true
        }
        else {
            binding.progressLoader.visibility = View.GONE
        }
    }

    private fun setCreateLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.createLoading.visibility = View.VISIBLE
        }
        else {
            binding.createLoading.visibility = View.GONE
            //Toast.makeText(requireContext(), "Book created!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError(message: String) {
        if (viewModel.data.value?.isEmpty() == true) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            binding.recyclerViewBooks.isVisible = false
        }
    }

    private fun processBooks(books: List<Book>) {
        booksAdapter.setData(books)
        binding.recyclerViewBooks.isVisible = true
    }

    private fun handleSwipe() {
        if (viewModel.loading.value == false) {
            viewModel.fetchBooks()
        }
        binding.swipe.isRefreshing = false
    }
}