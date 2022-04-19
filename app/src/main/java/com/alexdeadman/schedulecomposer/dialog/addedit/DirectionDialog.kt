package com.alexdeadman.schedulecomposer.dialog.addedit

import android.view.LayoutInflater
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FieldsDirectionBinding
import com.alexdeadman.schedulecomposer.viewmodel.DirectionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DirectionDialog : AbstractAddEditDialog<FieldsDirectionBinding>() {

    override val entityTitleId = R.string.direction
    override val mainViewModelClass = DirectionsViewModel::class
    override val relatedViewModelClass = null

    override fun createBinding(inflater: LayoutInflater) = FieldsDirectionBinding.inflate(inflater)
}