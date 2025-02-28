package com.example.librarymanagementapp.confirmation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment


class AlertDialogConfirmation() : DialogFragment() {

    interface AlertDialogCallback {
        fun onPositiveClick()
        fun onNegativeClick()
    }

    private var callback: AlertDialogCallback? = null

    companion object {
        fun newInstance(callback: AlertDialogCallback): AlertDialogConfirmation {
            val fragment = AlertDialogConfirmation()
            fragment.callback = callback
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { _, _ ->
                callback?.onPositiveClick()
                dismiss()
            }
            .setNegativeButton("Cancel") { _, _ ->
                callback?.onNegativeClick()
                dismiss()
            }
            .create()
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        try {
//            callback = context as AlertDialogCallback
//        } catch (e: ClassCastException) {
//            throw ClassCastException("$context must implement AlertDialogCallback")
//        }
//    }

}