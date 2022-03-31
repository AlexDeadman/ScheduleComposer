package com.alexdeadman.schedulecomposer.dialog

import android.content.Context
import android.os.Bundle
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.DialogLecturerBinding

class LecturerDialog(context: Context) : AbstractBottomSheetDialog<DialogLecturerBinding>(context) {

    override fun createBinding() {
        _binding = DialogLecturerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            // // // TODO TEMPO
            textViewAddEdit.text = context.getString(
                R.string.add_entity,
                context.getString(R.string.title_lecturers)
            )
            buttonAddEdit.text = context.getString(R.string.add)
            // // //
        }
    }
}