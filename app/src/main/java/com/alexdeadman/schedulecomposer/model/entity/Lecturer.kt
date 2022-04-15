package com.alexdeadman.schedulecomposer.model.entity

import com.alexdeadman.schedulecomposer.R
import com.google.gson.annotations.SerializedName


data class Lecturer(
    override var type: String,
    override var id: Int,
    override var attributes: LecturerAttributes,
    override var relationships: LecturerRelationships? = null
) : Entity<Lecturer.LecturerAttributes>, Relatable<Lecturer.LecturerRelationships> {

    constructor(id: Int, attributes: LecturerAttributes) : this("Lecturer", id, attributes)

    override val title get() = attributes.run { "$surname $firstName ${patronymic.orEmpty()}" }
    override val iconId get() = R.drawable.ic_lecturer

    override val detailsPhId: Int get() = R.string.ph_lecturer_details

    override fun getDetails(relatives: List<Entity<out Attributes>>): List<String> =
        listOf(
            relatives.filter {
                it.id in relationships!!.disciplines.data.map { dis -> dis.id }
            }.joinToString(";\n") { it.title }
        )

    data class LecturerAttributes(
        @SerializedName("first_name") var firstName: String,
        var surname: String,
        var patronymic: String?,

        var disciplines: List<Int>? = null
    ) : Attributes

    data class LecturerRelationships(var disciplines: Disciplines) : Relationships {
        data class Disciplines(var data: List<Data>) {
            data class Data(var id: Int)
        }
    }
}

