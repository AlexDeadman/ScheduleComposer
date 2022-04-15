package com.alexdeadman.schedulecomposer.model.entity

import com.alexdeadman.schedulecomposer.R
import com.google.gson.annotations.SerializedName

data class Syllabus(
    override var type: String,
    override var id: Int,
    override var attributes: SyllabusAttributes,
    override var relationships: SyllabusRelationships? = null,
) : Entity<Syllabus.SyllabusAttributes>, Relatable<Syllabus.SyllabusRelationships> {

    constructor(id: Int, attributes: SyllabusAttributes) : this("Syllabus", id, attributes)

    override val title get() = attributes.run { "$year $name" }
    override val iconId get() = R.drawable.ic_syllabus

    override val detailsPhId: Int get() = R.string.ph_syllabus_details

    override fun getDetails(relatives: List<Entity<out Attributes>>): List<String> =
         attributes.run {
            listOf(
                name,
                code
            ).plus(
                relatives.single { it.id == relationships!!.direction.data.id }.title
            )
        }

    data class SyllabusAttributes(
        var year: String,
        @SerializedName("specialty_code") var code: String,
        @SerializedName("specialty_name") var name: String,

        var direction: Int? = null
    ) : Attributes

    data class SyllabusRelationships(var direction: Direction) : Relationships {
        data class Direction(var data: Data) {
            data class Data(var id: Int)
        }
    }
}
