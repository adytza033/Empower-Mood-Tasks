package com.example.EmpowerTasksAndMood.Model

data class Diary(
    val DiaryID: Int,
    val UserID: Int,
    var DiaryContent: String,
    var DiaryTitle: String,
    var DiaryEntryDate: Int,
    var DiaryFaceID: Int
) {

}
