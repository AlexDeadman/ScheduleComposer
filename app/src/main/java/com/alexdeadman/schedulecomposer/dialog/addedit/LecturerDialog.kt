package com.alexdeadman.schedulecomposer.dialog.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FieldsLecturerBinding
import com.alexdeadman.schedulecomposer.model.entity.Lecturer
import com.alexdeadman.schedulecomposer.util.isValid
import com.alexdeadman.schedulecomposer.util.toStringOrNull
import com.alexdeadman.schedulecomposer.util.validate
import com.alexdeadman.schedulecomposer.viewmodel.AbstractEntityViewModel
import com.alexdeadman.schedulecomposer.viewmodel.LecturersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class LecturerDialog : AbstractAddEditDialog<FieldsLecturerBinding>() {

    override val entityTitleId = R.string.lecturer
    override val mainViewModelClass = LecturersViewModel::class
    override val relatedViewModelClass: KClass<out AbstractEntityViewModel>? = null

    override fun createBinding(inflater: LayoutInflater) = FieldsLecturerBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldsBinding.apply {

            val regex = """\s*[a-zA-Zа-яА-ЯёЁ-]*\s*""".toRegex()

            val validators: List<(String) -> Pair<Boolean, String>> = listOf(
                { it.isNotBlank() to getString(R.string.required_field) },
                { (it.length <= 200) to getString(R.string.wrong_symbol_count) },
                { (regex matches it) to getString(R.string.wrong_format) }
            )

            val tiLayouts = listOf(tiLayoutSurname, tiLayoutName, tiLayoutPatronymic)

            freezingViews = tiLayouts

            tiLayouts.run {
                take(2).forEach { it.validate(validators) }
                last().validate(validators.drop(1))
            }

            (currentEntity as Lecturer?)?.attributes?.let { attr ->
                tiLayouts
                    .map { it.editText!! }
                    .zip(listOf(attr.surname, attr.firstName, attr.patronymic))
                    .forEach { it.first.setText(it.second) }
            }

            binding.apply {
                buttonAddEdit.setOnClickListener {
                    if (tiLayouts.all { it.isValid() }) {
                        send(
                            Lecturer(
                                currentEntity?.id ?: -1,
                                Lecturer.LecturerAttributes(
                                    tiEditTextName.text.toString().trim(),
                                    tiEditTextSurname.text.toString().trim(),
                                    tiEditTextPatronymic.text.toStringOrNull()?.trim(),
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}