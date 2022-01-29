package com.example.auddistandroid.ui.lecturers.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auddistandroid.R
import com.example.auddistandroid.data.model.LecturersList
import com.example.auddistandroid.data.model.LecturersList.Lecturer
import com.example.auddistandroid.databinding.ItemLecturerBinding
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection

class LecturersAdapter(
    private val lecturersList: LecturersList
) : RecyclerView.Adapter<LecturersAdapter.LecturersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LecturersViewHolder {
        val binding = ItemLecturerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LecturersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LecturersViewHolder, position: Int) {
        holder.bind(lecturersList.data[position], holder.itemView.context)
    }

    override fun getItemCount(): Int = lecturersList.data.size

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
