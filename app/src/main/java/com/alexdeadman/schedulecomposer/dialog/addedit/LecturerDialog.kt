package com.alexdeadman.schedulecomposer.dialog.addedit

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FieldsLecturerBinding
import com.alexdeadman.schedulecomposer.databinding.ThemedButtonBinding
import com.alexdeadman.schedulecomposer.model.entity.Lecturer
import com.alexdeadman.schedulecomposer.util.isValid
import com.alexdeadman.schedulecomposer.util.state.ListState
import com.alexdeadman.schedulecomposer.util.toStringOrNull
import com.alexdeadman.schedulecomposer.util.validate
import com.alexdeadman.schedulecomposer.viewmodel.DisciplinesViewModel
import com.alexdeadman.schedulecomposer.viewmodel.LecturersViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton

@AndroidEntryPoint
class LecturerDialog : AbstractAddEditDialog<FieldsLecturerBinding>() {

    override val entityTitleId = R.string.lecturer
    override val mainViewModelClass = LecturersViewModel::class
    override val relatedViewModelClass = DisciplinesViewModel::class

    override fun createBinding(inflater: LayoutInflater) = FieldsLecturerBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldsBinding.apply {

//            currentEntity?.let {
//                (it.attributes as Lecturer.LecturerAttributes).run {
//                    tiEditTextName.setText(firstName)
//                    tiEditTextSurname.setText(surname)
//                    tiEditTextPatronymic.setText(patronymic)
//                }
//            }

            val regex = """\s*[a-zA-Zа-яА-ЯёЁ-]*\s*""".toRegex()

            val validators: List<(String) -> Pair<Boolean, String>> = listOf(
                { it.isNotBlank() to getString(R.string.required_field) },
                { (it.length <= 200) to getString(R.string.wrong_symbol_count) },
                { (regex matches it) to getString(R.string.wrong_format) }
            )

            val tiLayouts = listOf(tiLayoutSurname, tiLayoutName, tiLayoutPatronymic)

            tiLayouts.take(2).forEach { it.validate(validators) }
            tiLayoutPatronymic.validate(validators.drop(1))

            val relatives = (relatedViewModel!!.state.value as ListState.Loaded).result.data

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
                    ThemedButtonBinding.inflate(layoutInflater).root.apply {
                        applyToTexts {
                            it.apply {
                                maxLines = 1
                                ellipsize = TextUtils.TruncateAt.END
                            }
                        }
                        text = relative.title
                    },
                    resources.run {
                        RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            getDimensionPixelSize(R.dimen.themed_button_height)
                        ).apply { topMargin = getDimensionPixelSize(R.dimen.themed_button_margin) }
                    }
                )
            }

            val selectedButtons = toggleGroupDisciplines.selectedButtons

            binding.buttonAddEdit.setOnClickListener {
                if (tiLayouts.all(TextInputLayout::isValid)) {
                    if (selectedButtons.isNotEmpty()) {
                        binding.progressBar.visibility = View.VISIBLE

                        val selectedTitles = selectedButtons.map(ThemedButton::text)
                        Lecturer(
                            currentEntity?.id ?: -1,
                            Lecturer.LecturerAttributes(
                                tiEditTextName.text.toString().trim(),
                                tiEditTextSurname.text.toString().trim(),
                                tiEditTextPatronymic.text.toStringOrNull()?.trim(),
                                relatives.filter { it.title in selectedTitles }.map { it.id }
                            )
                        ).let {
                            mainViewModel.run {
                                if (isNew) postEntity(it)
                                else putEntity(it)
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