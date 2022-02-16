package com.example.auddistandroid.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.auddistandroid.R
import com.example.auddistandroid.data.model.entities.Entity
import com.example.auddistandroid.data.model.entities.Lecturer
import com.example.auddistandroid.databinding.ListItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ListItem(private val item: Entity) : AbstractBindingItem<ListItemBinding>() {

    override var identifier: Long
        get() = item.hashCode().toLong()
        set(value) {}

    override val type: Int
        get() = R.id.recycler_view

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ListItemBinding {
        return ListItemBinding.inflate(inflater, parent, false)
    }


    override fun bindView(binding: ListItemBinding, payloads: List<Any>) {
        binding.apply {
            when (item) {
                is Lecturer -> {
                    item.attributes.apply {
                        // maybe cringe
                        textViewItem.text = "$surname $firstName ${patronymic.orEmpty()}"
                    }

                    textViewItem.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_lecturer_24, 0, 0, 0
                    )
                }

                // TODO other entities
            }
        }
    }
}