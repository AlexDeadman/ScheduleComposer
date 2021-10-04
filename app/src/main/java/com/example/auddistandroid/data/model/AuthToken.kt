package com.example.auddistandroid.data.model

import com.google.gson.annotations.SerializedName

data class AuthToken(
    var data: Data
) {
    data class Data(
        var type: String,
        var id: String,
        var attributes: Attributes
    ) {
        data class Attributes(
            @SerializedName("auth_token") var authToken: String
        )
    }
}
