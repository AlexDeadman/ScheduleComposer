//package com.example.auddistandroid.data.model
//
//import com.google.gson.annotations.SerializedName
//
//
//data class Group(
//    var type: String,
//    var id: Int,
//    var attributes: GroupAttributes,
//    var relationships: GroupRelationships
//) : Entity {
//
//    data class GroupAttributes(
//        var number: String,
//        @SerializedName("students_count") var studentsCount: Int
//    ) : Attributes
//
//    data class GroupRelationships(var syllabus: Syllabus) : Relationships {
//
//        data class Syllabus(var data: Data) {
//
//            data class Data(var id: Int)
//        }
//    }
//}
//
