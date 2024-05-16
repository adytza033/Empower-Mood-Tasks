package com.example.EmpowerTasksAndMood.Model

data class Admin(
    val AdminId: Int,
    var AdminFullName: String,
    var AdminEmail: String,
    var AdminPhoneNo: String,
    var AdminUserName: String,
    var AdminPassword: String
) {

    override fun toString(): String {
        return "admin=(full name = '$AdminFullName', phone number = '$AdminPhoneNo', email = '$AdminEmail', username = '$AdminUserName', password = '$AdminPassword')"
    }
}