package com.alexdeadman.schedulecomposer.model.entity

import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.utils.toStringOrDash
import com.google.gson.annotations.SerializedName


data class Classroom(
    override var type: String,
    override var id: Int,
    override var attributes: AudienceAttributes,
    override var relationships: Relationships?,
) : Entity<Classroom.AudienceAttributes, Relationships> {

    override val title get() = attributes.number
    override val iconId get() = R.drawable.ic_audience

    override val detailsPhId: Int get() = R.string.ph_classroom_details
    override val details
        get() = attributes.run {
            mutableListOf(
                type.toStringOrDash(),
                seatsCount.toString()
            )
        }

    data class AudienceAttributes(
        var number: String,
        var type: String?,
        @SerializedName("seats_count") var seatsCount: Int,
    ) : Attributes
}
