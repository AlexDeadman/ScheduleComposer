package com.alexdeadman.schedulecomposer.model.entity

import com.alexdeadman.schedulecomposer.R
import com.google.gson.annotations.SerializedName


data class Group(
    override var type: String,
    override var id: Int,
    override var attributes: GroupAttributes,
    override var relationships: GroupRelationships? = null,
) : Entity<Group.GroupAttributes>, Relatable<Group.GroupRelationships> {

    constructor(id: Int, attributes: GroupAttributes) : this("Group", id, attributes)

    override val title get() = attributes.number
    override val iconId get() = R.drawable.ic_group

    override val detailsPhId: Int get() = R.string.ph_group_details

    override fun getDetails(relatives: List<Entity<out Attributes>>): List<String> =
        listOf(
            attributes.studentsCount.toString()
        ).plus(
            relatives.single { it.id == relationships!!.syllabus.data.id }.title
        )

    data class GroupAttributes(
        var number: String,
        @SerializedName("students_count") var studentsCount: Int,

        var syllabus: Int? = null,
    ) : Attributes

    data class GroupRelationships(var syllabus: Syllabus) : Relationships {

        data class Syllabus(var data: Data) {
            data class Data(var id: Int)
        }
    }
}

