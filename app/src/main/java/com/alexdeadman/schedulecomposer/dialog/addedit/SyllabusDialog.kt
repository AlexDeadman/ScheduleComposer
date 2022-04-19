package com.alexdeadman.schedulecomposer.dialog.addedit

import android.view.LayoutInflater
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FieldsSyllabusBinding
import com.alexdeadman.schedulecomposer.viewmodel.DirectionsViewModel
import com.alexdeadman.schedulecomposer.viewmodel.SyllabusesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SyllabusDialog : AbstractAddEditDialog<FieldsSyllabusBinding>() {

    override val entityTitleId = R.string.syllabus
    override val mainViewModelClass = SyllabusesViewModel::class
    override val relatedViewModelClass = DirectionsViewModel::class

    override fun createBinding(inflater: LayoutInflater) = FieldsSyllabusBinding.inflate(inflater)
}