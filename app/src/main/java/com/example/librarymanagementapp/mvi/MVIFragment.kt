package com.example.librarymanagementapp.mvi

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
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagementapp.UiState
import com.example.librarymanagementapp.databinding.FragmentMviBinding
import com.example.librarymanagementapp.models.Book
import com.example.librarymanagementapp.BooksAdapter
import kotlinx.coroutines.launch

class MVIFragment : Fragment() {
    private lateinit var binding: FragmentMviBinding
    private val viewModel by viewModels<ViewModelMVI>()
    private val adapter = BooksAdapter()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMviBinding.inflate(layoutInflater)
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

        binding.borrowBtn.setOnClickListener {
            viewModel.processIntent(MyIntent.BorrowBook)
        }

        binding.returnBtn.setOnClickListener {
            viewModel.processIntent(MyIntent.ReturnBook)
        }
        binding.recyclerViewBooks.adapter = adapter
    }

    private fun handleState(state: UiState) {
        println("qweqwe handleState $state")
        when (state) {
            is UiState.Loading -> {
                binding.loadingTv.visibility = View.VISIBLE
            }
            is UiState.Data -> {
                println("qweqwe handleState ${state.data.first()}")
                adapter.setData(state.data)
                binding.loadingTv.visibility = View.GONE
                binding.borrowBtn.isEnabled = true
                binding.returnBtn.isEnabled = true
            }
            is UiState.Error -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                binding.loadingTv.visibility = View.GONE
            }
        }
    }

}