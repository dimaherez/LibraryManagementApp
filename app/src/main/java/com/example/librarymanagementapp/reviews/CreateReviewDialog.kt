package com.example.librarymanagementapp.reviews

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.domain.models.Review
import com.example.librarymanagementapp.databinding.DialogCreateReviewBinding
import com.example.librarymanagementapp.info.BookInfoFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime


@AndroidEntryPoint
class CreateReviewDialog: DialogFragment() {
    private lateinit var binding: DialogCreateReviewBinding
    private val viewModel: ReviewsViewModel by viewModels()
    private val args: BookInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCreateReviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPostReview.setOnClickListener {
            if (validateInputs()) {
                postReview()
                dismiss()
                Toast.makeText(requireContext(), "Review Created", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(): Boolean {

        var isValid = true

        if (TextUtils.isEmpty(binding.userEditText.text)) {
            binding.userEditText.error = "Name is required"
            isValid = false
        } else {
            binding.userEditText.error = null
        }

        if (TextUtils.isEmpty(binding.reviewEditText.text)) {
            binding.reviewEditText.error = "Review is required"
            isValid = false
        } else {
            binding.reviewEditText.error = null
        }


        return isValid
    }

    private fun postReview() {
        val newReview = Review(
            author = binding.userEditText.text.toString(),
            rating = binding.ratingBar.rating.toInt(),
            date = LocalDateTime.now(),
            content = binding.reviewEditText.text.toString()
        )

        viewModel.processIntent(ReviewsIntent.PostReview(args.bookId, newReview))
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


}