package com.example.EmpowerTasksAndMood.Model

data class DailyQ(
    val DailyQuestionsID: Int,
    val UserID: Int,
    var DailyQuestionsEntryDate: Int,
    var DailyQuestionsSatisfactionProgress: String,
    var DailyQuestionsGrateful: String,
    var DailyQuestionsGoal: String,
    var DailyQuestionsOverallMood: Int
) {
}