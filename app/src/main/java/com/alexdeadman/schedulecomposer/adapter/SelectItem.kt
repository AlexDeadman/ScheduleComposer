package com.alexdeadman.schedulecomposer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.SelectItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class SelectItem(
    val title: String,
    val iconId: Int? = null
) : AbstractBindingItem<SelectItemBinding>() {

    override var identifier: Long
        get() = title.hashCode().toLong()
        set(value) {
            super.identifier = value
        }

    override val type: Int get() = R.id.select_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): SelectItemBinding =
        SelectItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: SelectItemBinding, payloads: List<Any>) {
        binding.apply {
            textViewTitle.apply {
                iconId?.let { setCompoundDrawablesRelativeWithIntrinsicBounds(it, 0, 0, 0) }
                text = title
            }
        }
    }
}