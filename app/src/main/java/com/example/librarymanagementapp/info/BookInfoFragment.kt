package com.example.librarymanagementapp.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.enums.string
import com.example.domain.models.Book
import com.example.librarymanagementapp.NavGraphDirections
import com.example.librarymanagementapp.R
import com.example.librarymanagementapp.databinding.FragmentBookInfoBinding

class BookInfoFragment : Fragment() {
    private lateinit var binding: FragmentBookInfoBinding
    private val viewModel: BookInfoViewModel by viewModels()
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

        showInfo(args.book)

        binding.btnEdit.setOnClickListener {
            findNavController()
                .navigate(BookInfoFragmentDirections.actionBookInfoFragmentToEditBookFragment())
        }

        binding.btnReviews.setOnClickListener {
            findNavController()
                .navigate(BookInfoFragmentDirections.actionBookInfoFragmentToReviewsFragment())
        }

        binding.btnAlertDialog.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalMyDialogFragment())
        }
    }

    private fun showInfo(book: Book) {
        binding.tvTitle.text = book.title
        binding.tvAuthor.text = book.author
        binding.tvReleaseDate.text = book.releaseDate.toString()
        binding.tvGenre.text = book.genre.string()
        binding.tvAvailability.text = if (book.isAvailable) "Available" else "Not Available"
        binding.tvPrice.text = book.price.toString()
        binding.tvRating.text = book.rating.toString()
        binding.tvDescription.text = book.description
    }
}