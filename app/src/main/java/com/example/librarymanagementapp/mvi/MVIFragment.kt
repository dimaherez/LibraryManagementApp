package com.example.librarymanagementapp.mvi

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
import com.example.data.Repo
import com.example.librarymanagementapp.databinding.FragmentMviBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MVIFragment : Fragment() {
    private lateinit var binding: FragmentMviBinding
    private val viewModel by viewModels<ViewModelMVI>()
    private val booksAdapter = MVIAdapter(
        onBorrowClick = { id -> viewModel.processIntent(MyIntent.BorrowBook(id))},
        onReturnClick = {id -> viewModel.processIntent(MyIntent.ReturnBook(id))}
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMviBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Repo.init()


        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleState(state)
                }
            }
        }

//        binding.borrowBtn.setOnClickListener {
//            viewModel.processIntent(MyIntent.BorrowBook(1))
//        }
//
//        binding.returnBtn.setOnClickListener {
//            viewModel.processIntent(MyIntent.ReturnBook(1))
//        }

        binding.swipe.setOnRefreshListener {
            handleSwipe()
        }

        binding.recyclerViewBooks.adapter = booksAdapter
    }

    private fun handleState(state: UiState) {
        when (state) {
            is UiState.Loading -> {
                binding.progressLoader.visibility = View.VISIBLE
                if (state.status == LoadingStatus.AFTER_ERROR) {
                    binding.recyclerViewBooks.isVisible = false // don't show list while loading if last fetching caused an error
                } else {
                    binding.recyclerViewBooks.isVisible = true // show list while loading after successful fetching
                }
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