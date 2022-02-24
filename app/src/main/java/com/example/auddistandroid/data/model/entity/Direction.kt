package com.example.auddistandroid.data.model.entity

data class Direction(
    var type: String,
    var id: Int,
    var attributes: Attributes,
) : Entity {

    data class Attributes(
        var code: String,
        var name: String,
    )
}
