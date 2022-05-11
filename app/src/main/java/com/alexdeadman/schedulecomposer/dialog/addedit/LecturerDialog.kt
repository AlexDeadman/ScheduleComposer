package com.alexdeadman.schedulecomposer.dialog.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FieldsLecturerBinding
import com.alexdeadman.schedulecomposer.model.entity.Lecturer
import com.alexdeadman.schedulecomposer.util.isValid
import com.alexdeadman.schedulecomposer.util.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.util.state.SendingState
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
            val tiEditTexts = tiLayouts.map { it.editText!! }

            tiLayouts.run {
                take(2).forEach { it.validate(validators) }
                last().validate(validators.drop(1))
            }

            if (currentEntity != null) {
                (currentEntity as Lecturer).attributes.let { attr ->
                    tiEditTexts
                        .zip(listOf(attr.surname, attr.firstName, attr.patronymic))
                        .forEach { it.first.setText(it.second) }
                }
            }

            binding.apply {
                mainViewModel.sendingStateFlow.launchRepeatingCollect(viewLifecycleOwner) { state ->
                    when (state) {
                        is SendingState.Default -> {}
                        is SendingState.Sending -> {
                            progressBar.visibility = View.VISIBLE
                            isCancelable = false
                            buttonAddEdit.isEnabled = false
                            tiEditTexts.forEach { it.isEnabled = false }
                        }
                        is SendingState.Success -> dismiss()
                        is SendingState.Error -> {
                            progressBar.visibility = View.INVISIBLE
                            isCancelable = true
                            buttonAddEdit.isEnabled = true
                            tiEditTexts.forEach { it.isEnabled = true }

                            Toast.makeText(
                                context,
                                getString(state.messageStringId),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                buttonAddEdit.setOnClickListener {
                    if (tiLayouts.all { it.isValid() }) {
                        Lecturer(
                            currentEntity?.id ?: -1,
                            Lecturer.LecturerAttributes(
                                tiEditTextName.text.toString().trim(),
                                tiEditTextSurname.text.toString().trim(),
                                tiEditTextPatronymic.text.toStringOrNull()?.trim(),
                            )
                        ).let {
                            mainViewModel.run {
                                if (currentEntity == null) postEntity(it)
                                else putEntity(it)
                            }
                        }
                    }
                }
            }
        }
    }
}