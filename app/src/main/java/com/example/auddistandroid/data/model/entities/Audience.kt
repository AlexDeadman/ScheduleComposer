package com.example.auddistandroid.data.model.entities

import com.google.gson.annotations.SerializedName


data class Audience(
    var type: String,
    var id: Int,
    var attributes: Attributes,
) : Entity {

    data class Attributes(
        var number: String,
        @SerializedName("aud_type") var audType: String?,
        @SerializedName("seats_count") var seatsCount: Int,
    )
}
