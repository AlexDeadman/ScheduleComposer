package com.alexdeadman.auddistandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexdeadman.auddistandroid.R
import com.alexdeadman.auddistandroid.data.model.entity.*
import com.alexdeadman.auddistandroid.databinding.ListItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ListItem(private val item: Entity) : AbstractBindingItem<ListItemBinding>() {

    @Suppress("UNUSED_PARAMETER")
    override var identifier: Long
        get() = item.hashCode().toLong()
        set(value) {}

    override val type: Int
        get() = R.id.recycler_view

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ListItemBinding =
        ListItemBinding.inflate(inflater, parent, false)


    override fun bindView(binding: ListItemBinding, payloads: List<Any>) {
        binding.apply {
            val pair = when (item) {
                is Audience -> item.attributes.number to R.drawable.ic_audience
                is Group -> item.attributes.number to R.drawable.ic_group
                is Discipline -> item.attributes.name to R.drawable.ic_discipline
                is Direction -> item.attributes.code to R.drawable.ic_direction

                is Lecturer -> item.attributes.run {
                    "$surname $firstName ${patronymic.orEmpty()}"
                } to R.drawable.ic_lecturer

                is Syllabus -> item.attributes.run {
                    "$year $specialtyName"
                } to R.drawable.ic_syllabus

                is Schedule -> "Schedule item" to R.drawable.ic_schedule
            }

            // TODO details

            textViewItem.apply {
                text = pair.first
                setCompoundDrawablesRelativeWithIntrinsicBounds(pair.second, 0, 0, 0)
            }
        }
    }
}