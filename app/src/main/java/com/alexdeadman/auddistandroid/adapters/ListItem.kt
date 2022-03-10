package com.alexdeadman.auddistandroid.adapters

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.alexdeadman.auddistandroid.R
import com.alexdeadman.auddistandroid.data.model.entity.*
import com.alexdeadman.auddistandroid.databinding.ListItemBinding
import com.mikepenz.fastadapter.expandable.ExpandableExtension.Companion.PAYLOAD_COLLAPSE
import com.mikepenz.fastadapter.expandable.ExpandableExtension.Companion.PAYLOAD_EXPAND
import com.mikepenz.fastadapter.expandable.items.AbstractExpandableItem

class ListItem(private val entity: Entity) : AbstractExpandableItem<ListItem.ViewHolder>() {

    override var identifier: Long
        get() = entity.hashCode().toLong()
        set(value) {
            super.identifier = value
        }

    override val type: Int
        get() = R.id.list_item_id

    override val layoutRes: Int
        get() = R.layout.list_item

    var title: String = ""
    private var angle = -180f

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        ListItemBinding.bind(holder.view).apply {

            val p = payloads.mapNotNull { it as? String }.lastOrNull()
            if (p == PAYLOAD_EXPAND || p == PAYLOAD_COLLAPSE) {
                ViewCompat.animate(imageViewArrow).rotationBy(angle).start()
                angle *= -1
            }

            val titleWithIcon = when (entity) {
                is Audience -> entity.attributes.number to R.drawable.ic_audience
                is Direction -> entity.attributes.code to R.drawable.ic_direction
                is Discipline -> entity.attributes.name to R.drawable.ic_discipline
                is Group -> entity.attributes.number to R.drawable.ic_group

                is Lecturer -> entity.attributes.run {
                    "$surname $firstName ${patronymic.orEmpty()}"
                } to R.drawable.ic_lecturer

                is Syllabus -> entity.attributes.run {
                    "$year $name"
                } to R.drawable.ic_syllabus

                else -> throw IllegalStateException()
            }

            textViewItem.apply {
                text = titleWithIcon.first
                setCompoundDrawablesRelativeWithIntrinsicBounds(
                    titleWithIcon.second, 0, 0, 0
                )
            }
            title = titleWithIcon.first
        }
    }

    fun withSubItem(listSubItem: ListSubItem): ListItem {
        subItems.add(listSubItem)
        return this
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)
}