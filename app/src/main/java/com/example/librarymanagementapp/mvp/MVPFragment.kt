package com.example.librarymanagementapp.mvp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librarymanagementapp.UiState
import com.example.librarymanagementapp.databinding.FragmentMvpBinding
import com.example.librarymanagementapp.models.Book
import com.example.librarymanagementapp.BooksAdapter


class MVPFragment : Fragment(), ShowBookView {
    private lateinit var binding: FragmentMvpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMvpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Presenter.attachView(this)

        binding.loadBooksBtn.setOnClickListener {
            Presenter.loadBooks()
        }
    }



    private fun handleState(state: UiState) {
        when (state) {
            is UiState.Loading -> binding.loadingTv.visibility = View.VISIBLE
            is UiState.Data -> {
                val recyclerView = binding.recyclerViewBooks
                recyclerView.layoutManager = LinearLayoutManager(context)
                val booksAdapter = BooksAdapter() {
                    book: Book ->  showBookDetailsDialog(book)
                }
                recyclerView.adapter = booksAdapter
                binding.loadingTv.visibility = View.GONE
            }
            is UiState.Error -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                binding.loadingTv.visibility = View.GONE

            }
        }
    }

    private fun showBookDetailsDialog(book: Book) {
        BookDetailsDialogFragment.newInstance(book).show(parentFragmentManager, "bookDetails")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Presenter.detachView()
    }

    override fun showBooks(books: List<Book>) {
        println("qweqwe showBooks")
        val recyclerView = binding.recyclerViewBooks
        recyclerView.layoutManager = LinearLayoutManager(context)
        val booksAdapter = BooksAdapter { book: Book ->  showBookDetailsDialog(book) }
        booksAdapter.setData(books)
        recyclerView.adapter = booksAdapter
        binding.recyclerViewBooks.isVisible = true
        binding.loadingTv.visibility = View.GONE
    }

    override fun showError(message: String) {
        println("qweqwe showError")
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        binding.recyclerViewBooks.isVisible = false
        binding.loadingTv.visibility = View.GONE
    }

    override fun showLoading() {
        println("qweqwe showLoading")
        binding.recyclerViewBooks.isVisible = false
        binding.loadingTv.visibility = View.VISIBLE
    }

}