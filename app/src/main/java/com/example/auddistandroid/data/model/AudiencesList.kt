package com.example.auddistandroid.data.model

import com.google.gson.annotations.SerializedName


data class AudiencesList (
    var data : List<Audience>
) {
    data class Audience(
        var type: String,
        var id: String,
        var attributes: Attributes,
    ) {
        data class Attributes (
            var number : String,
            @SerializedName("aud_type") var audType : String
        )
    }
}