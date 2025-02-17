package com.example.librarymanagementapp.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.librarymanagementapp.databinding.FragmentReviewsBinding
import com.example.librarymanagementapp.info.BookInfoFragmentArgs
import com.example.librarymanagementapp.mvi.BaseUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewsFragment : Fragment() {

    private lateinit var binding: FragmentReviewsBinding
    private val viewModel: ReviewsViewModel by viewModels()
    private val reviewsRvAdapter: ReviewsRvAdapter = ReviewsRvAdapter()

    private val args: BookInfoFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        returnHomeOnBackPress()

        viewModel.processIntent(ReviewsIntent.FetchBookById(args.bookId))

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleState(state)
                }
            }
        }

        binding.btnAddReview.setOnClickListener {
            findNavController().navigate(
                ReviewsFragmentDirections.actionReviewsFragmentToCreateReviewDialog(args.bookId)
            )

        }

        binding.swipe.setOnRefreshListener {
            handleSwipe()
        }

        binding.reviewsRv.adapter = reviewsRvAdapter
    }

    private fun handleState(state: BaseUiState) {
        when (state) {
            is BaseUiState.Loading -> {
                binding.progressLoader.visibility = View.GONE
            }
            is ReviewsState.BookData -> {
                reviewsRvAdapter.setData(state.book.reviews)
                binding.progressLoader.visibility = View.GONE
            }
            is BaseUiState.Error -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                binding.progressLoader.visibility = View.GONE
            }
        }
    }


    private fun returnHomeOnBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
    }

    private fun handleSwipe() {
        if(viewModel.uiState.value !is BaseUiState.Loading) {
            viewModel.processIntent(ReviewsIntent.FetchBookById(args.bookId))
        }
        binding.swipe.isRefreshing = false
    }
}