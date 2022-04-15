package com.alexdeadman.schedulecomposer.dialog

import android.os.Bundle
import android.view.View
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.LecturerFieldsBinding
import com.alexdeadman.schedulecomposer.model.entity.Lecturer
import com.alexdeadman.schedulecomposer.utils.isValid
import com.alexdeadman.schedulecomposer.utils.state.ListState
import com.alexdeadman.schedulecomposer.utils.toStringOrNull
import com.alexdeadman.schedulecomposer.utils.validate
import com.alexdeadman.schedulecomposer.viewmodels.DisciplinesViewModel
import com.alexdeadman.schedulecomposer.viewmodels.LecturersViewModel
import com.google.android.material.textfield.TextInputLayout
import com.validator.textinputvalidator.getValidator
import dagger.hilt.android.AndroidEntryPoint
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton

@AndroidEntryPoint
class LecturerDialog : AbstractCreateUpdateDialog<LecturerFieldsBinding>() {

    override val entityTitleId = R.string.lecturer
    override val mainViewModelClass = LecturersViewModel::class
    override val relatedViewModelClass = DisciplinesViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldsBinding = LecturerFieldsBinding.inflate(layoutInflater).apply {

            binding.frameLayoutFields.addView(root)

            entity?.let {
                (it.attributes as Lecturer.LecturerAttributes).run { // FIXME
                    tiEditTextName.setText(firstName)
                    tiEditTextSurname.setText(surname)
                    tiEditTextPatronymic.setText(patronymic)
                }
            }

            val regex = """\s*[a-zA-Zа-яА-ЯёЁ-]*\s*""".toRegex()
            val tiLayouts = listOf(tiLayoutSurname, tiLayoutName, tiLayoutPatronymic)

            tiLayouts.forEach { layout ->
                layout.validate(listOf(
                    { it.isNotBlank() to getString(R.string.required_field) },
                    { (it.length <= 200) to getString(R.string.wrong_symbol_count) },
                    { (regex matches it) to getString(R.string.wrong_format) }
                ))
            }
            tiLayoutPatronymic.apply {
                getValidator()!!.validators.drop(1).let { validate(it) }
                error = null
            }

            val relatives = (relatedViewModel!!.fetchState.value as ListState.Loaded).result.data

            toggleGroupDisciplines.apply {
                selectableAmount = relatives.size
                setOnSelectListener {
                    if (selectedButtons.size != 0) {
                        textViewDisciplinesError.visibility = View.INVISIBLE
                    }
                }
            }

            for (relative in relatives.sortedBy { it.title }) {
                toggleGroupDisciplines.addView(
                    customThemedButton.apply { text = relative.title },
                    layoutParams
                )
            }

            val selectedButtons = toggleGroupDisciplines.selectedButtons

            binding.buttonAddEdit.setOnClickListener {
                if (tiLayouts.all(TextInputLayout::isValid)) {
                    if (selectedButtons.isNotEmpty()) {
                        val selectedTitles = selectedButtons.map(ThemedButton::text)
                        Lecturer(
                            entity?.id ?: -1,
                            Lecturer.LecturerAttributes(
                                tiEditTextName.text.toString().trim(),
                                tiEditTextSurname.text.toString().trim(),
                                tiEditTextPatronymic.text.toStringOrNull()?.trim(),
                                relatives.filter { it.title in selectedTitles }.map { it.id }
                            )
                        ).let {
                            mainViewModel.run {
                                if (isNew) postEntity(it)
                                else updateEntity(it)
                            }
                        }
                    } else {
                        textViewDisciplinesError.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}