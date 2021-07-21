package com.example.auddistandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.auddistandroid.R
import com.example.auddistandroid.model.lecturers.Lecturer
import com.example.auddistandroid.model.lecturers.LecturersList

class LecturersAdapter(
    context: Context,
    private val lecturersList: LecturersList,
) : RecyclerView.Adapter<LecturersAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_lecturer, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = lecturersList.data.size

    private fun getItem(position: Int): Lecturer = lecturersList.data[position]

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fullName: TextView = itemView.findViewById(R.id.lecturer_item_text)

        fun bind(lecturer: Lecturer) {
            fullName.text = (
                    lecturer.attributes.surname + " " +
                            lecturer.attributes.firstName[0] + ". " +
                            lecturer.attributes.patronymic[0] + ". "
                    )
        }
    }
}
