package com.alexdeadman.schedulecomposer.dialog.addedit

import android.view.LayoutInflater
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FieldsDisciplineBinding
import com.alexdeadman.schedulecomposer.viewmodel.DisciplinesViewModel
import com.alexdeadman.schedulecomposer.viewmodel.SyllabusesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisciplineDialog : AbstractAddEditDialog<FieldsDisciplineBinding>() {

    override val entityTitleId = R.string.discipline
    override val mainViewModelClass = DisciplinesViewModel::class
    override val relatedViewModelClass = SyllabusesViewModel::class

    override fun createBinding(inflater: LayoutInflater) = FieldsDisciplineBinding.inflate(inflater)
}