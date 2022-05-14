package com.alexdeadman.schedulecomposer.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.alexdeadman.schedulecomposer.R

class ConfirmationDialog : DialogFragment() {

    interface ConfirmationListener {
        var confirmationMessage: String?
        fun onConfirm()
    }

    private lateinit var listener: ConfirmationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as ConfirmationListener
        } catch (e: ClassCastException) {
            throw ClassCastException(parentFragment.toString() + " must implement ConfirmationListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.confirm_action)
            .setMessage(listener.confirmationMessage ?: getString(R.string.are_you_sure))
            .setPositiveButton(R.string.yes) { _, _ -> listener.onConfirm() }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
}