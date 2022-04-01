package com.alexdeadman.schedulecomposer.dialog

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.DialogLecturerBinding
import com.alexdeadman.schedulecomposer.utils.isValid
import com.alexdeadman.schedulecomposer.utils.validate
import com.google.android.material.textfield.TextInputLayout

class LecturerDialog(
    context: Context,
//    relatives: List<Entity<out Attributes>>,
//    entity: Entity<out Attributes>? = null
) : AbstractBottomSheetDialog<DialogLecturerBinding>(context, listOf(), null) {

    override fun createBinding() {
        _binding = DialogLecturerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {

            // TODO REFACTORING (move something to parent)

            val entityTitle = context.getString(R.string.title_lecturers)

            if (entity != null) {
                textViewAddEdit.text = context.getString(R.string.edit_entity, entityTitle)
                buttonAddEdit.text = context.getString(R.string.edit)
            } else {
                textViewAddEdit.text = context.getString(R.string.add_entity, entityTitle)
                buttonAddEdit.text = context.getString(R.string.add)
            }

            val regex = """[a-zA-Zа-яА-ЯёЁ]{1,30}""".toRegex()

            val tiLayouts = listOf(tiLayoutSurname, tiLayoutName, tiLayoutPatronymic)

            tiLayouts.forEach {
                it.validate(listOf(
                    { text -> text.isNotBlank() to context.getString(R.string.required_field) },
                    { text -> (regex matches text) to context.getString(R.string.wrong_format) }
                ))
            }

            buttonAddEdit.setOnClickListener {
                if (tiLayouts.all(TextInputLayout::isValid)) {
                    acTextViewDisciplines.setAdapter(
                        ArrayAdapter(
                            context,
                            R.layout.dropdown_item,
                            relatives.map { it.title }
                        )
                    )
                }
            }
        }
    }
}