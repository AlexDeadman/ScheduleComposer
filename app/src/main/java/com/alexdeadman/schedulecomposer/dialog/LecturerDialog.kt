package com.alexdeadman.schedulecomposer.dialog

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RelativeLayout
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.DialogLecturerBinding
import com.alexdeadman.schedulecomposer.databinding.ThemedButtonBinding
import com.alexdeadman.schedulecomposer.model.entity.Attributes
import com.alexdeadman.schedulecomposer.model.entity.Entity
import com.alexdeadman.schedulecomposer.utils.isValid
import com.alexdeadman.schedulecomposer.utils.validate
import com.google.android.material.textfield.TextInputLayout
import com.validator.textinputvalidator.getValidator

class LecturerDialog(
    context: Context,
    relatives: List<Entity<out Attributes>>,
    entity: Entity<out Attributes>? = null,
) : AbstractBottomSheetDialog<DialogLecturerBinding>(context, relatives, entity) {

    override fun createBinding() {
        _binding = DialogLecturerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {

            // // // TODO REFACTOR (move to parent)
            val entityTitleId = context.getString(R.string.lecturer)
            if (entity != null) {
                textViewAddEdit.text = context.getString(R.string.edit_entity, entityTitleId)
                buttonAddEdit.text = context.getString(R.string.edit)
            } else {
                textViewAddEdit.text = context.getString(R.string.add_entity, entityTitleId)
                buttonAddEdit.text = context.getString(R.string.add)
            }
            // // //

            val regex = """\s*[a-zA-Zа-яА-ЯёЁ-]*\s*""".toRegex()
            val tiLayouts = listOf(tiLayoutSurname, tiLayoutName, tiLayoutPatronymic)

            tiLayouts.forEach { layout ->
                layout.validate(listOf(
                    { it.isNotBlank() to context.getString(R.string.required_field) },
                    { (it.length <= 200) to context.getString(R.string.wrong_symbol_count) },
                    { (regex matches it) to context.getString(R.string.wrong_format) }
                ))
            }

            tiLayoutPatronymic.run {
                getValidator()!!.validators.drop(1).let { validate(it) }
                error = null
            }

            toggleGroupDisciplines.selectableAmount = relatives.size


            for (entity in relatives) {
                val themedButton = ThemedButtonBinding.inflate(layoutInflater).root.apply {
                    applyToTexts {
                        it.run {
                            maxLines = 1
                            ellipsize = TextUtils.TruncateAt.END
                        }
                    }
                    text = entity.title
                }

                val params = context.resources.run {
                    RelativeLayout.LayoutParams(
                        WRAP_CONTENT,
                        getDimensionPixelSize(R.dimen.themed_button_height)
                    ).apply {
                        topMargin = getDimensionPixelSize(R.dimen.themed_button_margin)
                    }
                }

                toggleGroupDisciplines.addView(themedButton, params)
            }

            buttonAddEdit.setOnClickListener {
                if (tiLayouts.all(TextInputLayout::isValid)) {
                    // TODO
                }
            }
        }
    }
}