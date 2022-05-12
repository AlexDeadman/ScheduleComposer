package com.alexdeadman.schedulecomposer.dialog.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FieldsScheduleBinding
import com.alexdeadman.schedulecomposer.viewmodel.AbstractEntityViewModel
import com.alexdeadman.schedulecomposer.viewmodel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class ScheduleDialog : AbstractAddEditDialog<FieldsScheduleBinding>() {

    override val entityTitleId = R.string.schedule
    override val mainViewModelClass = ScheduleViewModel::class
    override val relatedViewModelClass: KClass<out AbstractEntityViewModel>? = null

    override fun createBinding(inflater: LayoutInflater) = FieldsScheduleBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldsBinding.textView.text = mainViewModel.currentEntity.toString()
    }
}