package com.alexdeadman.schedulecomposer.data.model.entity

import com.alexdeadman.schedulecomposer.R
import com.google.gson.annotations.SerializedName

data class Syllabus(
    override var type: String,
    override var id: Int,
    override var attributes: SyllabusAttributes,
    override var relationships: SyllabusRelationships?
) : Entity<Syllabus.SyllabusAttributes, Syllabus.SyllabusRelationships> {

    override val title get() = attributes.run { "$year $name" }
    override val iconId get() = R.drawable.ic_syllabus

    override val detailsPhId: Int get() = R.string.ph_syllabus_details
    override val details get() = mutableListOf(attributes.code)

    data class SyllabusAttributes(
        var year: String,
        @SerializedName("specialty_code") var code: String,
        @SerializedName("specialty_name") var name: String,
    ) : Attributes

    data class SyllabusRelationships(var direction: Direction) : Relationships {

        data class Direction(var data: Data) {

            data class Data(var id: Int)
        }
    }
}
