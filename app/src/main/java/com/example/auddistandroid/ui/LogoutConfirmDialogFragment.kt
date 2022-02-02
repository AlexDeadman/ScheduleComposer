package com.example.auddistandroid.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.R

class LogoutConfirmDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.are_you_sure))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                preferences.edit().apply {
                    remove("authToken")
                    remove("username")
                    apply()
                }
                requireActivity().finish()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
    }
}