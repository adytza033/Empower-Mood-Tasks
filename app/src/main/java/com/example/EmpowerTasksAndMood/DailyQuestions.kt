package com.example.EmpowerTasksAndMood

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.DailyQ
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyQuestions : AppCompatActivity() {
    private lateinit var editTextProgress: EditText
    private lateinit var editTextGrateful: EditText
    private lateinit var editTextGoal: EditText

    val dbHelper: DBHelper = DBHelper(this)
    var userId: Int = 0

    private var selectedFaceId: Int = 0

    private val faceDrawableToIdMap = mapOf(
        R.id.face1 to 1,
        R.id.face2  to 2,
        R.id.face3  to 3,
        R.id.face4 to 4,
        R.id.face5 to 5,

        1 to R.id.face1,
        2 to R.id.face2,
        3 to R.id.face3,
        4 to R.id.face4,
        5 to R.id.face5
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_questions)

        editTextProgress = findViewById(R.id.editTextSatisfaction)
        editTextGrateful = findViewById(R.id.editTextGrateful)
        editTextGoal = findViewById(R.id.editTextGoal)

        userId = intent.getIntExtra("user_ID", 0)

        val face1: ImageView = findViewById(R.id.face1)
        val face2: ImageView = findViewById(R.id.face2)
        val face3: ImageView = findViewById(R.id.face3)
        val face4: ImageView = findViewById(R.id.face4)
        val face5: ImageView = findViewById(R.id.face5)

        face1.setOnClickListener { onFaceSelected(it) }
        face2.setOnClickListener { onFaceSelected(it) }
        face3.setOnClickListener { onFaceSelected(it) }
        face4.setOnClickListener { onFaceSelected(it) }
        face5.setOnClickListener { onFaceSelected(it) }

        if (intent.hasExtra("DailyQID")) {
            populate()
        }
    }


    private fun populate() {
        val dailyQID = intent.getIntExtra("DailyQID", -1)
        userId = intent.getIntExtra("UserID", -1)
        val progress = intent.getStringExtra("SatisfactionProgress") ?: ""
        val grateful = intent.getStringExtra("Grateful") ?: ""
        val goal = intent.getStringExtra("Goal")
        val mood = intent.getIntExtra("OverallMood", -1)

        if (dailyQID != -1) {
            editTextProgress.setText(progress)
            editTextGrateful.setText(grateful)
            editTextGoal.setText(goal)

            findViewById<ImageView>(R.id.face1).alpha = 0.3f
            findViewById<ImageView>(R.id.face2).alpha = 0.3f
            findViewById<ImageView>(R.id.face3).alpha = 0.3f
            findViewById<ImageView>(R.id.face4).alpha = 0.3f
            findViewById<ImageView>(R.id.face5).alpha = 0.3f

            selectedFaceId = faceDrawableToIdMap[mood] ?: return
            findViewById<ImageView>(selectedFaceId).alpha = 1.0f

        }
    }

    fun onFaceSelected(view: View) {
        selectedFaceId = view.id
        updateFaceSelection(view.id)
    }

    private fun updateFaceSelection(selectedId: Int) {
        findViewById<ImageView>(R.id.face1).alpha = 0.3f
        findViewById<ImageView>(R.id.face2).alpha = 0.3f
        findViewById<ImageView>(R.id.face3).alpha = 0.3f
        findViewById<ImageView>(R.id.face4).alpha = 0.3f
        findViewById<ImageView>(R.id.face5).alpha = 0.3f

        findViewById<ImageView>(selectedId).alpha = 1.0f
    }

    fun saveDailyQuestions(view: View) {
        val progressText = editTextProgress.text.toString().trim()
        val gratefulText = editTextGrateful.text.toString().trim()
        val goalText = editTextGoal.text.toString().trim()

        val existingDailyQId = intent.getIntExtra("DailyQID", -1)

        if (progressText.isEmpty() || gratefulText.isEmpty() || goalText.isEmpty()) {
            Toast.makeText(this, "All fields must be filled in.", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedFaceId == 0) {
            Toast.makeText(this, "Select one face that represents your mood.", Toast.LENGTH_SHORT).show()
            return
        }

        val mood = faceDrawableToIdMap[selectedFaceId] ?: return
        val answer = true

        if(!checkDateExists(getCurrentDate(), userId)) {
            val dailyQuestions = DailyQ(0, userId, getCurrentDate(), progressText, gratefulText, goalText, mood)
            val success = dbHelper.insertDailyQEntry(dailyQuestions)
            if (success) {
                answer
            }
        }

        if(existingDailyQId != -1) {
            dailyfolderPage()
        } else if (answer == true) {
            Toast.makeText(this, "Entry saved successfully.", Toast.LENGTH_SHORT).show()
            dailyfolderPage()
        }
        else {
            Toast.makeText(this, "Failed to save the entry.", Toast.LENGTH_SHORT).show()
        }
    }


    fun burnDailyQEntry(view: View) {
        AlertDialog.Builder(this).apply {
            setTitle("Burn Daily Questions Entry")
            setMessage("Are you sure you want to burn this daily questions entry? This action cannot be undone.")
            setPositiveButton("Burn") { dialog, which ->
                val dailyQID = intent.getIntExtra("DailyQID", -1)
                if (dailyQID != -1) {
                    val success = dbHelper.deleteDailyQEntry(dailyQID)
                    if (success) {
                        Toast.makeText(context, "Diary entry burned successfully", Toast.LENGTH_SHORT).show()
                        dailyfolderPage()
                    } else {
                        Toast.makeText(context, "Failed to burn diary entry", Toast.LENGTH_SHORT).show()
                    }
                }
                finish()
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    private fun checkDateExists(todayDate: Int, userId: Int): Boolean {
        val newcursor: Cursor? = dbHelper.getDailyQEntries()
        while(newcursor!!.moveToNext()) {
            val UserID = newcursor.getInt(1)
            val EntryDate = newcursor.getInt(2)

            if (formatDiaryEntryDate(EntryDate) == formatDiaryEntryDate(todayDate) && userId == UserID) {
                return true
            }
        }
        newcursor?.close()
        return false
    }

    private fun formatDiaryEntryDate(timestamp: Int): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp * 1000L))
    }

    private fun getCurrentDate(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }
    fun menuPage(view: View){
        val intent = Intent(this, Menu::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

    fun dailyfolderPage() {
        val intent = Intent(this, DailyQFolder::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }
}
