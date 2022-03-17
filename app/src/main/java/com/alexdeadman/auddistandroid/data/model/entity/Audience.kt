package com.alexdeadman.auddistandroid.data.model.entity

import com.alexdeadman.auddistandroid.R
import com.alexdeadman.auddistandroid.utils.toStringOrDash
import com.google.gson.annotations.SerializedName


data class Audience(
    override var type: String,
    override var id: Int,
    override var attributes: AudienceAttributes,
    override var relationships: Relationships?,
) : Entity<Audience.AudienceAttributes, Relationships> {

    override val title get() = attributes.number
    override val iconId get() = R.drawable.ic_audience

    override val detailsPhId: Int get() = R.string.ph_audience_details
    override val details
        get() = attributes.run {
            mutableListOf(
                audType.toStringOrDash(),
                seatsCount.toString()
            )
        }

    data class AudienceAttributes(
        var number: String,
        @SerializedName("aud_type") var audType: String?,
        @SerializedName("seats_count") var seatsCount: Int,
    ) : Attributes
}
