package com.alexdeadman.schedulecomposer.data.model.entity

interface Attributes
interface Relationships

interface Entity<A : Attributes, R : Relationships> {
    var type: String
    var id: Int
    var attributes: A
    var relationships: R?

    val title: String
    val iconId: Int

    val detailsPhId: Int
    val details: MutableList<String>
}