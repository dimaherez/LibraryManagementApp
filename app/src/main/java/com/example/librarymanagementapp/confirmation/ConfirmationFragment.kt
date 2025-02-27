package com.example.librarymanagementapp.confirmation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment


class ConfirmationFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { _, _ ->

                dismiss()
            }
            .setNegativeButton("Cancel") { _, _ ->

                dismiss()
            }
            .create()
    }

}