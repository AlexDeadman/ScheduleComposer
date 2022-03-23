package com.alexdeadman.schedulecomposer.model.entity

import com.alexdeadman.schedulecomposer.R
import com.google.gson.annotations.SerializedName


data class Lecturer(
    override var type: String,
    override var id: Int,
    override var attributes: LecturerAttributes,
    override var relationships: LecturerRelationships?
) : Entity<Lecturer.LecturerAttributes, Lecturer.LecturerRelationships> {

    override val title get() = attributes.run { "$surname $firstName ${patronymic.orEmpty()}" }
    override val iconId get() = R.drawable.ic_lecturer

    override val detailsPhId: Int get() = R.string.ph_lecturer_details
    override val details get() = mutableListOf("")

    data class LecturerAttributes(
        @SerializedName("first_name") var firstName: String,
        var surname: String,
        var patronymic: String?,
    ) : Attributes

    data class LecturerRelationships(var disciplines: Disciplines) : Relationships {
        data class Disciplines(var data: List<Data>) {
            data class Data(var id: Int)
        }
    }
}

