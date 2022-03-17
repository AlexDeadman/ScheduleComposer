//package com.alexdeadman.auddistandroid.adapters
//
//import android.view.View
//import androidx.recyclerview.widget.RecyclerView
//import com.alexdeadman.auddistandroid.R
//import com.alexdeadman.auddistandroid.R.string.*
//import com.alexdeadman.auddistandroid.data.model.entity.*
//import com.alexdeadman.auddistandroid.databinding.ListSubItemBinding
//import com.alexdeadman.auddistandroid.utils.toStringOrDash
//import com.mikepenz.fastadapter.ISubItem
//import com.mikepenz.fastadapter.expandable.items.AbstractExpandableItem
//
//class ListSubItem(
//    private val entity: Entity<out Attributes, out Relationships>,
////    private val relatedEntities: List<Entity>
//) : AbstractExpandableItem<ListSubItem.ViewHolder>(), ISubItem<ListSubItem.ViewHolder> {
//
//    override val type: Int
//        get() = R.id.list_sub_item_id
//
//    override val layoutRes: Int
//        get() = R.layout.list_sub_item
//
//    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)
//
//    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
//        super.bindView(holder, payloads)
//
//        // TODO related entities
//        // TODO refactor
//
//        ListSubItemBinding.bind(holder.view).textViewDetails.text =
//            holder.view.resources.run {
//                when (entity) {
//                    is Audience -> entity.attributes.run {
//                        getString(
//                            ph_audience_details,
//                            audType.toStringOrDash(),
//                            seatsCount
//                        )
//                    }
//                    is Direction -> {
//                        getString(
//                            ph_direction_details,
//                            entity.attributes.name
//                        )
//                    }
//                    is Discipline -> entity.attributes.run {
//                        getString(
//                            ph_discipline_details,
//                            code,
//                            cycle,
//                            hoursTotal,
//                            hoursLec.toStringOrDash(),
//                            hoursPr.toStringOrDash(),
//                            hoursLa.toStringOrDash(),
//                            hoursIsw.toStringOrDash(),
//                            hoursCons.toStringOrDash()
//                        )
//                    }
//                    is Group -> {
//                        getString(
//                            ph_group_details,
//                            entity.attributes.studentsCount
//                        )
//                    }
//                    is Lecturer -> ""
//                    is Syllabus -> {
//                        getString(
//                            ph_syllabus_details,
//                            entity.attributes.code
//                        )
//                    }
//                    else -> throw IllegalStateException()
//                }
//            }
//    }
//
//    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
//}
