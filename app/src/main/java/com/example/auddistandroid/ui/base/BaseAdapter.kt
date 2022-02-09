package com.example.auddistandroid.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auddistandroid.R
import com.example.auddistandroid.data.model.DataList
import com.example.auddistandroid.data.model.Entity
import com.example.auddistandroid.data.model.entities.Lecturer
import com.example.auddistandroid.databinding.ItemLecturerBinding

class BaseAdapter(
    private val dataList: DataList<Lecturer> // TODO TEMPO
) : RecyclerView.Adapter<BaseAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemLecturerBinding.inflate(
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
        private val binding: ItemLecturerBinding // TODO TEMPO
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Entity, context: Context) {
            binding.apply {
                // TODO TEMPO
                (item as Lecturer).attributes.apply {
                    lecturerItemText.text = context.getString(
                        R.string.fullname_ph,
                        surname,
                        firstName,
                        patronymic.orEmpty()
                    )
                }
            }
        }
    }
}
