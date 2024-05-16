package com.example.EmpowerTasksAndMood

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Adapters.DiaryFolderAdapter
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Diary

class DiaryFolder : AppCompatActivity() {

    private lateinit var recyclerViewDiaryEntities: RecyclerView
    private lateinit var diaryList: ArrayList<Diary>

    val dbHelper: DBHelper = DBHelper(this)
    var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_folder)

        recyclerViewDiaryEntities = findViewById(R.id.recycleViewDiaryEntities)
        recyclerViewDiaryEntities.layoutManager = LinearLayoutManager(this)

        userId = intent.getIntExtra("user_ID", 0)

        displayDiaryEntities()
    }

    private fun displayDiaryEntities(){
        val newcursor: Cursor? = dbHelper.getDiaryEntries()
        diaryList = ArrayList<Diary>()
        while(newcursor!!.moveToNext()) {
            val DiaryID = newcursor.getInt(0)
            val UserID = newcursor.getInt(1)
            val DiaryContent = newcursor.getString(2)
            val DiaryTitle = newcursor.getString(3)
            val DiaryEntryDate = newcursor.getInt(4)
            val DiaryFaceID = newcursor.getInt(5)

            if(UserID == userId ) {
                diaryList.add(Diary(DiaryID, UserID, DiaryContent, DiaryTitle,DiaryEntryDate,DiaryFaceID))
            }
        }
        recyclerViewDiaryEntities.adapter = DiaryFolderAdapter(diaryList)
    }

    fun diaryEntry(view:View){
        val intent = Intent(this, com.example.EmpowerTasksAndMood.Diary::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }
    fun menuPage(view:View){
        val intent = Intent(this, Menu::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }
}
