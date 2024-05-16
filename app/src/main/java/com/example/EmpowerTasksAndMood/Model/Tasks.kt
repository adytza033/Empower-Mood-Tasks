package com.example.EmpowerTasksAndMood.Model

data class Tasks(
    val TasksId: Int,
    val UserID: Int,
    var FolderID: Int,
    var TasksDescription: String,
    var TasksDueDate: String,
    var TasksPriority: String,
    var TasksStatus: Boolean
) {

}

