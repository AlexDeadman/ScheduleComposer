package com.alexdeadman.schedulecomposer.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.utils.PreferenceKeys
import com.alexdeadman.schedulecomposer.utils.requireGrandParentFragment

class LogoutConfirmDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle(R.string.log_out)
            .setMessage(getString(R.string.are_you_sure))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                preferences.edit().apply {
                    remove(PreferenceKeys.AUTH_TOKEN)
                    remove(PreferenceKeys.USERNAME)
                    apply()
                }
                requireGrandParentFragment()
                    .findNavController()
                    .navigate(R.id.action_mainFragment_to_connectionFragment)
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
    }
}