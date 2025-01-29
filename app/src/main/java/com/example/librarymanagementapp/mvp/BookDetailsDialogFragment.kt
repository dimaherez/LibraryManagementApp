package com.example.librarymanagementapp.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.librarymanagementapp.databinding.FragmentDetailsBinding
import com.example.librarymanagementapp.models.Book

class BookDetailsDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentDetailsBinding

    companion object {
        fun newInstance(book: Book): BookDetailsDialogFragment {
            val fragment = BookDetailsDialogFragment()
            val args = Bundle().apply { putString("book_info", book.toString()) }

            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bookInfo = arguments?.getString("book_info")
        if (bookInfo != null) {
            binding.bookInfo.text = bookInfo
        }
    }
}
