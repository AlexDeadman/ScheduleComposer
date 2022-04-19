package com.alexdeadman.schedulecomposer.dialog.addedit

import android.view.LayoutInflater
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FieldsClassroomBinding
import com.alexdeadman.schedulecomposer.viewmodel.ClassroomsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassroomDialog : AbstractAddEditDialog<FieldsClassroomBinding>() {

    override val entityTitleId = R.string.classroom
    override val mainViewModelClass = ClassroomsViewModel::class
    override val relatedViewModelClass = null

    override fun createBinding(inflater: LayoutInflater) = FieldsClassroomBinding.inflate(inflater)
}