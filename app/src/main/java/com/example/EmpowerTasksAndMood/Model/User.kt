package com.example.EmpowerTasksAndMood.Model

data class User(
    val UserId: Int,
    var UserFullName: String,
    var UserEmail: String,
    var UserPhoneNo: String,
    var UserUserName: String,
    var UserPassword: String,
    var UserIsActive: Boolean
) {

    override fun toString(): String {
        return "customer=(full name = '$UserFullName', email = '$UserEmail', phone number = '$UserPhoneNo', username = '$UserUserName', password = '$UserPassword')"
    }
}

