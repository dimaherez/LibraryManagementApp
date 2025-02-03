package com.example.librarymanagementapp.mvp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.librarymanagementapp.databinding.FragmentMvpBinding
import com.example.librarymanagementapp.models.Book


class MVPFragment : Fragment(), BooksView {
    private lateinit var binding: FragmentMvpBinding
    private val booksAdapter = MVPAdapter { book: Book ->  toggleFavorite(book) }

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

        Presenter.fetchBooks()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                booksAdapter.filterBooks(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                Presenter.filterBooks(newText)
                booksAdapter.filterBooks(newText)
                return false
            }
        })

        binding.swipe.setOnRefreshListener {
            handleSwipe()
        }

        binding.recyclerViewBooks.adapter = booksAdapter
    }

    private fun showBookDetailsDialog(book: Book) {
        BookDetailsDialogFragment.newInstance(book).show(parentFragmentManager, "bookDetails")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Presenter.detachView()
    }

    override fun showBooks(books: List<Book>) {
        Log.d("mylog", "showBooks()")
        booksAdapter.setData(books)

        binding.recyclerViewBooks.isVisible = true
        binding.progressLoader.visibility = View.GONE
    }

    override fun showError(message: String) {
        Log.d("mylog", "showError()")
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        binding.recyclerViewBooks.isVisible = false
        binding.progressLoader.visibility = View.GONE

    }

    override fun showLoading() {
        Log.d("mylog", "showLoading()")
        binding.progressLoader.visibility = View.VISIBLE
//        binding.recyclerViewBooks.isVisible = false
        binding.searchView.clearFocus()
    }

    private fun toggleFavorite(book: Book) {
        Presenter.toggleFavorite(book)
    }

    private fun handleSwipe() {
        Presenter.fetchBooks()
        binding.swipe.isRefreshing = false
    }

}