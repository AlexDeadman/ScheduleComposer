package com.example.auddistandroid.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.auddistandroid.R
import com.example.auddistandroid.data.model.entity.*
import com.example.auddistandroid.databinding.ListItemBinding
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
            val pair: Pair<String, Int> = when (item) {
                is Audience -> Pair(item.attributes.number, R.drawable.ic_audience)
                is Group -> Pair(item.attributes.number, R.drawable.ic_group)
                is Discipline -> Pair(item.attributes.name, R.drawable.ic_discipline)
                is Direction -> Pair(item.attributes.code, R.drawable.ic_direction)

                is Lecturer -> Pair(
                    item.attributes.run { "$surname $firstName ${patronymic.orEmpty()}" },
                    R.drawable.ic_lecturer
                )

                is Syllabus -> Pair(
                    item.attributes.run { "$year $specialtyName" },
                    R.drawable.ic_syllabus
                )

                else -> Pair("", R.drawable.ic_question_mark)
            }

            // TODO details

            textViewItem.apply {
                text = pair.first
                setCompoundDrawablesRelativeWithIntrinsicBounds(pair.second, 0, 0, 0)
            }
        }
    }
}