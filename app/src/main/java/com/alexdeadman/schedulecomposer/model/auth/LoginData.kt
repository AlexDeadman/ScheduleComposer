package com.alexdeadman.schedulecomposer.model.auth

data class LoginData(
    var data: Data,
) {
    data class Data(
        var type: String,
        var attributes: Attributes,
    ) {
        data class Attributes(
            var username: String,
            var password: String,
        )
    }
}
