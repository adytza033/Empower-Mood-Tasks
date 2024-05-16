package com.example.EmpowerTasksAndMood

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Adapters.DailyQFolderAdapter
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.DailyQ

class DailyQFolder : AppCompatActivity() {

    private lateinit var recyclerViewDailyQEntities: RecyclerView
    private lateinit var dailyQList: ArrayList<DailyQ>

    val dbHelper: DBHelper = DBHelper(this)
    var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_questions_folder)

        recyclerViewDailyQEntities = findViewById(R.id.recycleViewDailyQuestionsEntities)
        recyclerViewDailyQEntities.layoutManager = LinearLayoutManager(this)

        userId = intent.getIntExtra("user_ID", 0)

        displayDiaryEntities()
    }

    private fun displayDiaryEntities(){
        val newcursor: Cursor? = dbHelper.getDailyQEntries()
        dailyQList = ArrayList<DailyQ>()
        while(newcursor!!.moveToNext()) {
            val DailyQuestionsID = newcursor.getInt(0)
            val UserID = newcursor.getInt(1)
            val EntryDate = newcursor.getInt(2)
            val SatisfactionProgress = newcursor.getString(3)
            val Grateful = newcursor.getString(3)
            val Goal = newcursor.getString(3)
            val OverallMood = newcursor.getInt(6)

            if(UserID == userId) {
                dailyQList.add(DailyQ(DailyQuestionsID, UserID, EntryDate, SatisfactionProgress, Grateful, Goal, OverallMood))
            }
        }
        recyclerViewDailyQEntities.adapter = DailyQFolderAdapter(dailyQList)
    }

    fun menuPage(view: View){
        val intent = Intent(this, Menu::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }
}
