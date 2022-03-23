package com.alexdeadman.schedulecomposer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.data.model.entity.Attributes
import com.alexdeadman.schedulecomposer.data.model.entity.Entity
import com.alexdeadman.schedulecomposer.data.model.entity.Relationships
import com.alexdeadman.schedulecomposer.databinding.ListItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ListItem(
    private val entity: Entity<out Attributes, out Relationships>
) : AbstractBindingItem<ListItemBinding>() {

    override var identifier: Long
        get() = entity.id.toLong()
        set(value) {
            super.identifier = value
        }

    override val type: Int
        get() = R.id.list_item_id

    val entityTitle = entity.title

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ListItemBinding =
        ListItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ListItemBinding, payloads: List<Any>) {
        binding.apply {
            entity.run {
                textViewTitle.apply {
                    setCompoundDrawablesRelativeWithIntrinsicBounds(iconId, 0, 0, 0)
                    text = title
                }
                textViewDetails.text = root.resources.getString(
                    detailsPhId,
                    *details.toTypedArray()
                )
            }
        }
    }
}