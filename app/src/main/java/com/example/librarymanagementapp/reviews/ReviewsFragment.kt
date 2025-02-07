package com.example.librarymanagementapp.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.librarymanagementapp.NavGraphDirections
import com.example.librarymanagementapp.databinding.FragmentReviewsBinding

class ReviewsFragment : Fragment() {

    private lateinit var binding: FragmentReviewsBinding
    private val viewModel: ReviewsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        returnHomeOnBackPress()

        binding.btnAlertDialog.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalMyDialogFragment())
        }
    }

    private fun returnHomeOnBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action = ReviewsFragmentDirections.actionReviewsFragmentToBooksFragment()
                    findNavController().navigate(action)

//                    findNavController().popBackStack(R.id.booksFragment, false)
                }
            })
    }
}