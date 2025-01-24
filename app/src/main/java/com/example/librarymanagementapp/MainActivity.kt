package com.example.librarymanagementapp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val scope = CoroutineScope(Dispatchers.IO)
    val viewModel: LibraryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.books.observe(this) {
            binding.tv1.text = it.joinToString("\n")
        }

        viewModel.sorted.observe(this) {
            binding.tv2.text = it.toString()
        }

        binding.nonparallelButton.setOnClickListener {
            resetTextViews()
            viewModel.nonParallelCall()

        }

        binding.parallelButton.setOnClickListener {
            resetTextViews()
            viewModel.parallelCall()
        }

        Log.d("mylog", "onCreate Triggered")
    }

    private fun resetTextViews() {
        binding.tv1.text = "Loading..."
        binding.tv2.text = "Loading..."
    }
}