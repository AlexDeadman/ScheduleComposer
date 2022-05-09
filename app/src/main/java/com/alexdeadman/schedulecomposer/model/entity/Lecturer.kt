package com.alexdeadman.schedulecomposer.model.entity

import com.alexdeadman.schedulecomposer.R
import com.google.gson.annotations.SerializedName


data class Lecturer(
    override var type: String,
    override var id: Int,
    override var attributes: LecturerAttributes,
) : Entity<Lecturer.LecturerAttributes>{

    constructor(id: Int, attributes: LecturerAttributes) : this("Lecturer", id, attributes)

    val shortTitle
        get() = attributes.run {
            "$surname ${firstName.first()}." + patronymic?.firstOrNull()?.plus(".").orEmpty()
        }

    override val title get() = attributes.run { "$surname $firstName ${patronymic.orEmpty()}" }
    override val iconId get() = R.drawable.ic_lecturer

    override val detailsId: Int? = null

    override fun getDetails(relatives: List<Entity<out Attributes>>): List<String> = emptyList()

    data class LecturerAttributes(
        @SerializedName("first_name") var firstName: String,
        var surname: String,
        var patronymic: String?,
    ) : Attributes
}

