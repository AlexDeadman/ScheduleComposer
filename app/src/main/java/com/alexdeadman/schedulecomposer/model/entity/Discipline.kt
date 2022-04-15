package com.alexdeadman.schedulecomposer.model.entity

import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.utils.toStringOrDash
import com.google.gson.annotations.SerializedName


data class Discipline(
    override var type: String,
    override var id: Int,
    override var attributes: DisciplineAttributes,
    override var relationships: DisciplineRelationships? = null
) : Entity<Discipline.DisciplineAttributes>, Relatable<Discipline.DisciplineRelationships> {

    constructor(id: Int, attributes: DisciplineAttributes) : this("Discipline", id, attributes)

    override val title get() = attributes.name
    override val iconId get() = R.drawable.ic_discipline

    override val detailsPhId: Int get() = R.string.ph_discipline_details

    override fun getDetails(relatives: List<Entity<out Attributes>>): List<String> =
        attributes.run {
            listOf(
                name,
                code,
                cycle,
                hoursTotal.toString(),
                hoursLec.toStringOrDash(),
                hoursPr.toStringOrDash(),
                hoursLa.toStringOrDash(),
                hoursIsw.toStringOrDash(),
                hoursCons.toStringOrDash()
            ).plus(
                relatives.single { it.id == relationships!!.syllabus.data.id }.title
            )
        }

    data class DisciplineAttributes(
        var name: String,
        var code: String,
        var cycle: String,
        @SerializedName("hours_total") var hoursTotal: Int,
        @SerializedName("hours_lec") var hoursLec: Int?,
        @SerializedName("hours_pr") var hoursPr: Int?,
        @SerializedName("hours_la") var hoursLa: Int?,
        @SerializedName("hours_isw") var hoursIsw: Int?,
        @SerializedName("hours_cons") var hoursCons: Int?,

        var syllabus: Int? = null
    ) : Attributes

    data class DisciplineRelationships(var syllabus: Syllabus) : Relationships {

        data class Syllabus(var data: Data) {
            data class Data(var id: Int)
        }
    }
}

