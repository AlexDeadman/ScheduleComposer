package com.example.auddistandroid.data.model

import com.google.gson.annotations.SerializedName

data class LecturersList(
    @SerializedName("data") var data: List<Lecturer>,
) {
    data class Lecturer(
        @SerializedName("type") var type: String,
        @SerializedName("id") var id: String,
        @SerializedName("attributes") var attributes: Attributes,
        @SerializedName("relationships") var relationships: Relationships
    ) {
        data class Attributes(
            @SerializedName("first_name") var firstName: String,
            @SerializedName("surname") var surname: String,
            @SerializedName("patronymic") var patronymic: String
        )

        data class Relationships(
            @SerializedName("disciplines") var disciplines: Disciplines
        ) {
            data class Disciplines(
                @SerializedName("data") var data: List<Data>
            ) {
                data class Data(
                    @SerializedName("type") var type: String,
                    @SerializedName("id") var id: String
                )
            }
        }
    }
}
