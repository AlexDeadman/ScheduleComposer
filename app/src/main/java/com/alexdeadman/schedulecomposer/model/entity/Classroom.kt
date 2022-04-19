package com.alexdeadman.schedulecomposer.model.entity

import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.util.toStringOrDash
import com.google.gson.annotations.SerializedName


data class Classroom(
    override var type: String,
    override var id: Int,
    override var attributes: ClassroomAttributes,
) : Entity<Classroom.ClassroomAttributes> {

    constructor(id: Int, attributes: ClassroomAttributes) : this("Classroom", id, attributes)

    override val title get() = attributes.number
    override val iconId get() = R.drawable.ic_classroom

    override val detailsPhId: Int get() = R.string.ph_classroom_details

    override fun getDetails(relatives: List<Entity<out Attributes>>): List<String> =
        attributes.run {
            listOf(
                type.toStringOrDash(),
                seatsCount.toString()
            )
        }

    data class ClassroomAttributes(
        var number: String,
        var type: String?,
        @SerializedName("seats_count") var seatsCount: Int,
    ) : Attributes
}
