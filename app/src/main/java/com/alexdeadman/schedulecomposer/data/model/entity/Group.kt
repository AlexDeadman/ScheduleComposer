package com.alexdeadman.schedulecomposer.data.model.entity

import com.alexdeadman.schedulecomposer.R
import com.google.gson.annotations.SerializedName


data class Group(
    override var type: String,
    override var id: Int,
    override var attributes: GroupAttributes,
    override var relationships: GroupRelationships?
) : Entity<Group.GroupAttributes, Group.GroupRelationships> {

    override val title get() = attributes.number
    override val iconId get() = R.drawable.ic_group

    override val detailsPhId: Int get() = R.string.ph_group_details
    override val details get() = mutableListOf(attributes.studentsCount.toString())

    data class GroupAttributes(
        var number: String,
        @SerializedName("students_count") var studentsCount: Int,
    ) : Attributes

    data class GroupRelationships(var syllabus: Syllabus) : Relationships {

        data class Syllabus(var data: Data) {

            data class Data(var id: Int)
        }
    }
}

