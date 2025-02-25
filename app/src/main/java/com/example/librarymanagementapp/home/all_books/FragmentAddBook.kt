package com.example.librarymanagementapp.home.all_books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.data.BooksDB
import com.example.domain.enums.Genre
import com.example.domain.models.Book
import com.example.domain.models.Review
import com.example.librarymanagementapp.databinding.DialogAddBookBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate

class FragmentAddBook : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAddBookBinding
    private val args: FragmentAddBookArgs by navArgs<FragmentAddBookArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newBook = Book(
            id = 0,
            title = "ATest Book ${BooksDB.books.size + 1}",
            genre = Genre.FICTION,
            author = "Test Author",
            releaseDate = LocalDate.of(1925, 4, 10),
            price = 10.99f,
            isAvailable = true,
            borrowCount = 5,
            availableCount = 5,
            isFavorite = false,
            description = "Some description",
            reviews = listOf(
                Review("User1", rating = 4, content = "Nice"),
                Review("User2", rating = 3, content = "Good")
            )
        )
        binding.tvBookInfo.text = newBook.toString()

        binding.btnAddBook.setOnClickListener {

        }
    }

}
