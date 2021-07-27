package com.example.auddistandroid.ui.lecturers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auddistandroid.data.model.LecturersList
import com.example.auddistandroid.data.model.LecturersList.Lecturer
import com.example.auddistandroid.databinding.ItemLecturerBinding

class LecturersAdapter(
    private val lecturersList: LecturersList
) : RecyclerView.Adapter<LecturersAdapter.LecturersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LecturersViewHolder {
        val binding = ItemLecturerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

       return LecturersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LecturersViewHolder, position: Int) {
        holder.bind(lecturersList.data[position])
    }

    override fun getItemCount(): Int = lecturersList.data.size

    inner class LecturersViewHolder(
        private val binding: ItemLecturerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(lecturer: Lecturer) {
            binding.apply {
                lecturer.attributes.apply {
                    lecturerItemText.text = ("$surname $firstName $patronymic")
                }
            }
        }
    }
}
