package com.example.librarymanagementapp.edit

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.enums.Genre
import com.example.domain.models.Book
import com.example.librarymanagementapp.databinding.FragmentEditBookBinding
import com.example.librarymanagementapp.info.BookInfoFragmentArgs
import com.example.librarymanagementapp.mvi.BaseUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@AndroidEntryPoint
class EditBookFragment : Fragment() {

    private lateinit var binding: FragmentEditBookBinding
    private val viewModel: EditBookViewModel by viewModels()
    private val args: BookInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBookBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.processIntent(EditBookIntent.FetchBookById(args.bookId))

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleState(state)
                }
            }
        }

        binding.saveButton.setOnClickListener {
            if (validateInputs()) saveBookChanges()
        }

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.editReleaseDate.setOnClickListener {
            showDatePickerDialog(binding.editReleaseDate)
        }
    }

    private fun saveBookChanges() {
        val currentState = viewModel.uiState.value
        if (currentState is BookEditState.BookData) {
            viewModel.processIntent(
                EditBookIntent.UpdateBook(
                    setBookNewData(currentState.book)
                )
            )
            Toast.makeText(requireContext(), "Book has been updated!", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun handleState(state: BaseUiState) {
        when (state) {
            is BaseUiState.Loading -> {
                binding.progressLoader.visibility = View.VISIBLE
            }

            is BookEditState.BookData -> {
                setInitialData(state.book)
                binding.progressLoader.visibility = View.GONE
            }

            is BaseUiState.Error -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                binding.progressLoader.visibility = View.GONE
            }
        }
    }

    private fun setInitialData(book: Book) {
        binding.editTitle.setText(book.title)
        binding.editAuthor.setText(book.author)
        binding.editPrice.setText(book.price.toString())
        binding.editDescription.setText(book.description)
        binding.editReleaseDate.setText(book.releaseDate.toString())

        binding.editGenreGroup.check(
            when (book.genre) {
                Genre.FANTASY -> binding.genreFantasy.id
                Genre.FICTION -> binding.genreFiction.id
                Genre.BIOGRAPHY -> binding.genreBiography.id
                Genre.SCIENCE_FICTION -> binding.genreScienceFiction.id
                Genre.MYSTERY -> binding.genreMystery.id
            }
        )
    }

    private fun setBookNewData(book: Book): Book {
        val newGenre = when (binding.editGenreGroup.checkedRadioButtonId) {
            binding.genreFantasy.id -> Genre.FANTASY
            binding.genreMystery.id -> Genre.MYSTERY
            binding.genreFiction.id -> Genre.FICTION
            binding.genreBiography.id -> Genre.BIOGRAPHY
            binding.genreScienceFiction.id -> Genre.SCIENCE_FICTION
            else -> book.genre
        }

        val newDate = try {
            LocalDate.parse(
                binding.editReleaseDate.text.toString(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
        } catch (e: IllegalArgumentException) {
            book.releaseDate
        }

        val newPrice = try {
            binding.editPrice.text.toString().toFloat()
        } catch (e: NumberFormatException) {
            book.price
        }


        return book.copy(
            title = binding.editTitle.text.toString().ifEmpty { book.title },
            author = binding.editAuthor.text.toString().ifEmpty { book.author },
            description = binding.editDescription.text.toString().ifEmpty { book.description },
            releaseDate = newDate,
            genre = newGenre,
            price = newPrice
        )
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (binding.editTitle.text.isNullOrEmpty()) {
            binding.titleTextInputLayout.error = "Title is required"
            isValid = false
        } else {
            binding.titleTextInputLayout.error = null
        }

        if (binding.editAuthor.text.isNullOrEmpty()) {
            binding.authorTextInputLayout.error = "Author is required"
            isValid = false
        } else {
            binding.authorTextInputLayout.error = null
        }

        if (binding.editReleaseDate.text.isNullOrEmpty()) {
            binding.releaseDateTextInputLayout.error = "Release date is required"
            isValid = false
        } else {
            binding.releaseDateTextInputLayout.error = null
        }

        if (binding.editPrice.text.isNullOrEmpty()) {
            binding.priceTextInputLayout.error = "Price is required"
            isValid = false
        } else {
            binding.priceTextInputLayout.error = null
        }

        if (binding.editDescription.text.isNullOrEmpty()) {
            binding.descriptionTextInputLayout.error = "Description is required"
            isValid = false
        } else {
            binding.descriptionTextInputLayout.error = null
        }

        return isValid
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the date as YYYY-MM-DD and set it to the EditText
                val formattedDate =
                    String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                editText.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

}