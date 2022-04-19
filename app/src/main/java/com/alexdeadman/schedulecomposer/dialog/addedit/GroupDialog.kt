package com.alexdeadman.schedulecomposer.dialog.addedit

import android.view.LayoutInflater
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FieldsGroupBinding
import com.alexdeadman.schedulecomposer.viewmodel.GroupsViewModel
import com.alexdeadman.schedulecomposer.viewmodel.SyllabusesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDialog : AbstractAddEditDialog<FieldsGroupBinding>() {

    override val entityTitleId = R.string.group
    override val mainViewModelClass = GroupsViewModel::class
    override val relatedViewModelClass = SyllabusesViewModel::class

    override fun createBinding(inflater: LayoutInflater) = FieldsGroupBinding.inflate(inflater)
}