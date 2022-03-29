package com.alexdeadman.schedulecomposer.model.entity

interface Attributes
interface Relationships

interface Entity<A : Attributes> {
    var type: String
    var id: Int
    var attributes: A

    val title: String
    val iconId: Int

    val detailsPhId: Int
    val details: MutableList<String>
}

interface Relatable<A: Relationships> {
    var relationships: A

    fun getRelativesTitles(relatives: List<Entity<out Attributes>>): List<String>
}