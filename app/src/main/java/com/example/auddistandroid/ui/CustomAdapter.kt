package com.example.auddistandroid.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auddistandroid.R
import com.example.auddistandroid.data.model.DataList
import com.example.auddistandroid.data.model.entities.Entity
import com.example.auddistandroid.data.model.entities.Lecturer
import com.example.auddistandroid.databinding.ListItemBinding

class CustomAdapter<T : Entity>(
    private val dataList: DataList<T>
) : RecyclerView.Adapter<CustomAdapter<T>.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(dataList.data[position], holder.itemView.context)
    }

    override fun getItemCount(): Int = dataList.data.size

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    inner class CustomViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Entity, context: Context) {
            binding.apply {
                when (item) {
                    is Lecturer -> {
                        item.attributes.apply {
                            @Suppress("UselessCallOnNotNull")
                            textViewItem.text = context.getString(
                                R.string.fullname_ph,
                                surname,
                                firstName,
                                patronymic.orEmpty()
                            )
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
}
