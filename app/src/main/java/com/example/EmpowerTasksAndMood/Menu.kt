package com.example.EmpowerTasksAndMood

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Tasks
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Menu : AppCompatActivity() {
    var userId: Int = 0
    val dbHelper: DBHelper = DBHelper(this)
    var numberTasks: Int = 0
    var numberTodayTasks: Int = 0
    var numberImportantTasks: Int = 0

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var allTasks: ArrayList<Tasks>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        sharedPreferences = getSharedPreferences("com.example.EmpowerTasksAndMood.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)

        userId = intent.getIntExtra("user_ID", 0)
        allTasksNumber()
        todayTasksNumber()
        allTasksImportant()
        helloName()
    }

    fun helloName() {
        val helloName = findViewById<TextView>(R.id.Hello)
        val name =  dbHelper.getUser(userId)
        val firstName = name.UserFullName.split(" ").firstOrNull()

        helloName.text = "Hello $firstName"
    }

    fun allTasksNumber() {
        val newcursor: Cursor? = dbHelper.getTasks()

        allTasks = ArrayList<Tasks>()
        while (newcursor!!.moveToNext()) {
            val userID = newcursor.getInt(1)
            if (userID == userId) {
                numberTasks++
            }
        }
        val tasks = findViewById<TextView>(R.id.textViewAllTasks)
        tasks.text = numberTasks.toString()
    }

    fun todayTasksNumber() {
        val newcursor: Cursor? = dbHelper.getTasks()
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        allTasks = ArrayList<Tasks>()
        while (newcursor!!.moveToNext()) {
            val userID = newcursor.getInt(1)
            val tasksDueDate = newcursor.getString(4)
            if (userID == userId && tasksDueDate == currentDate) {
                numberTodayTasks++
            }
        }
        val tasks = findViewById<TextView>(R.id.textViewTodayTasks)
        tasks.text = numberTodayTasks.toString()
    }


    fun allTasksImportant() {
        val newcursor: Cursor? = dbHelper.getTasks()
        allTasks = ArrayList<Tasks>()
        while (newcursor!!.moveToNext()) {
            val userID = newcursor.getInt(1)
            val tasksPriority = newcursor.getString(5)
            if (userID == userId && tasksPriority == "Priority1") {
                numberImportantTasks++
            }
        }
        val importantTasks = findViewById<TextView>(R.id.textViewImportantTasks)
        importantTasks.text = numberImportantTasks.toString()
    }

    fun mainPage(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

    fun allTasks(view: View) {
        val intent = Intent(this, AllTasks::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

    fun loginPage(view: View) {
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.remove("password")
        editor.apply()

        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    fun importantTasksPage(view: View) {
        val intent = Intent(this, ImportantTasks::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

    fun uppdateInfo(view: View) {
        val intent = Intent(this, Update_Delete::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

    fun DiaryFolderPage(view:View){
        val intent = Intent(this, DiaryFolder::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

    fun quotesPage(view:View){
        val intent = Intent(this, Quotes::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

    fun DailyQuestionsPage(view: View) {
        val currentDate = getCurrentDate()

        if (checkDateExists(currentDate, userId)) {
            val intent = Intent(this, DailyQFolder::class.java)
            intent.putExtra("user_ID", userId)
            startActivity(intent)
        } else {
            val intent = Intent(this, DailyQuestions::class.java)
            intent.putExtra("user_ID", userId)
            startActivity(intent)
        }
    }

    fun todayTasks(view: View) {
        val intent = Intent(this, TodayTasks::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

    private fun checkDateExists(todayDate: Int, userId: Int): Boolean {
        val newcursor: Cursor? = dbHelper.getDailyQEntries()
        while (newcursor!!.moveToNext()) {
            val UserID = newcursor.getInt(1)
            val EntryDate = newcursor.getInt(2)

            if (formatDiaryEntryDate(EntryDate) == formatDiaryEntryDate(todayDate) && userId == UserID) {
                return true
            }
        }
        newcursor?.close()
        return false
    }

    private fun getCurrentDate(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }

    private fun formatDiaryEntryDate(timestamp: Int): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp * 1000L))
    }

    fun report(view: View) {
        val intent = Intent(this, ReportFolder::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }
}