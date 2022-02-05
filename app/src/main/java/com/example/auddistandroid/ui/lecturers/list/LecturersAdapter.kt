package com.example.auddistandroid.ui.lecturers.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auddistandroid.R
import com.example.auddistandroid.data.model.DataList
import com.example.auddistandroid.data.model.Lecturer
import com.example.auddistandroid.databinding.ItemLecturerBinding

class LecturersAdapter(
    private val lecturerList: DataList<Lecturer>
) : RecyclerView.Adapter<LecturersAdapter.LecturersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LecturersViewHolder {
        val binding = ItemLecturerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LecturersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LecturersViewHolder, position: Int) {
        holder.bind(lecturerList.data[position], holder.itemView.context)
    }

    override fun getItemCount(): Int = lecturerList.data.size

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    inner class LecturersViewHolder(
        private val binding: ItemLecturerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(lecturer: Lecturer, context: Context) {
            binding.apply {
                lecturer.attributes.apply {
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
