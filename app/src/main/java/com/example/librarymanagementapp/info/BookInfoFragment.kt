package com.example.librarymanagementapp.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.enums.string
import com.example.domain.models.Book
import com.example.librarymanagementapp.R
import com.example.librarymanagementapp.databinding.FragmentBookInfoBinding
import com.example.librarymanagementapp.mvi.BaseUiState
import com.example.librarymanagementapp.reviews.ReviewsRvAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BookInfoFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBookInfoBinding
    private val viewModel: BookInfoViewModel by viewModels()
    private val reviewsRvAdapter: ReviewsRvAdapter = ReviewsRvAdapter()
    private val args: BookInfoFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookInfoBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.root.post {
//            val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            bottomSheet?.let {
//                val bottomSheetBehavior = BottomSheetBehavior.from(it)
//                val rowHeight = binding.linearLayoutReviews.height
//                bottomSheetBehavior.peekHeight = rowHeight
//            }
//        }


        binding.root.post {
            val bottomSheet = binding.constraintLayout.parent
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet as View)
            val rowHeight = binding.linearLayoutReviews.height
            bottomSheetBehavior.peekHeight = rowHeight
        }


        viewModel.processIntent(BookInfoIntent.FetchBookById(args.bookId))

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleState(state)
                }
            }
        }

        binding.btnEdit.setOnClickListener {
            findNavController()
                .navigate(BookInfoFragmentDirections.actionBookInfoFragmentToEditBookFragment(args.bookId))
        }

        binding.btnReviews.setOnClickListener {
            findNavController()
                .navigate(BookInfoFragmentDirections.actionBookInfoFragmentToReviewsFragment(args.bookId))
        }

        binding.rvReviews.adapter = reviewsRvAdapter

    }

    private fun handleState(state: BaseUiState){
        when(state){
            is BaseUiState.Loading -> {

            }
            is BookInfoState.BookData -> {
                reviewsRvAdapter.setData(state.book.reviews)
                showInfo(state.book)
            }
            is BaseUiState.Error -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showInfo(book: Book) {
        binding.tvTitle.text = book.title
        binding.tvAuthor.text = book.author
        binding.tvReleaseDate.text = book.releaseDate.toString()
        binding.tvGenre.text = book.genre.string()
        binding.tvAvailability.text = if (book.isAvailable) "Available" else "Not Available"
        binding.tvPrice.text = book.price.toString()
        binding.ratingBar.rating = book.rating.toFloat()
        binding.tvDescription.text = book.description
        binding.btnReviews.text = requireContext().resources.getQuantityString(
            R.plurals.plural_review, book.reviews.size, book.reviews.size
        )
    }
}