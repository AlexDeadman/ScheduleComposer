package com.example.auddistandroid.data.model

import com.google.gson.annotations.SerializedName

data class LoginData(
    var data: Data
) {
    data class Data(
        var type: String,
        var attributes: Attributes
    ) {
        data class Attributes(
           var username: String,
           var password: String
        )
    }
}
