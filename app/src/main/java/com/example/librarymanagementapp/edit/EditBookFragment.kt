package com.example.librarymanagementapp.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.librarymanagementapp.NavGraphDirections
import com.example.librarymanagementapp.databinding.FragmentEditBookBinding

class EditBookFragment : Fragment() {

    private lateinit var binding: FragmentEditBookBinding
    private val viewModel: EditBookViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBookBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAlertDialog.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalMyDialogFragment())
        }
    }
}