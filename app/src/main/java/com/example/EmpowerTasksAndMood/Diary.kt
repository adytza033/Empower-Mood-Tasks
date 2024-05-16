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
import com.example.EmpowerTasksAndMood.Model.Diary

class Diary : AppCompatActivity() {
    private lateinit var editTextDiaryTitle: EditText
    private lateinit var editTextDiaryContent: EditText
    private val dbHelper: DBHelper = DBHelper(this)
    private var userId: Int = 0

    private var selectedFaceId: Int = 0

    private val faceDrawableToIdMap = mapOf(
        R.id.face1 to 1,
        R.id.face2  to 2,
        R.id.face3  to 3,
        R.id.face4 to 4,

        1 to R.id.face1,
        2 to R.id.face2,
        3 to R.id.face3,
        4 to R.id.face4
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        editTextDiaryTitle = findViewById(R.id.diaryTitle)
        editTextDiaryContent = findViewById(R.id.diaryContent)

        userId = intent.getIntExtra("user_ID", 0)

        val face1: ImageView = findViewById(R.id.face1)
        val face2: ImageView = findViewById(R.id.face2)
        val face3: ImageView = findViewById(R.id.face3)
        val face4: ImageView = findViewById(R.id.face4)

        face1.setOnClickListener { onFaceSelected(it) }
        face2.setOnClickListener { onFaceSelected(it) }
        face3.setOnClickListener { onFaceSelected(it) }
        face4.setOnClickListener { onFaceSelected(it) }

        if (intent.hasExtra("DiaryID")) {
            populate()
        }
    }

    private fun populate() {
        val diaryId = intent.getIntExtra("DiaryID", -1)
        userId = intent.getIntExtra("UserID", -1)
        val diaryContent = intent.getStringExtra("DiaryContent") ?: ""
        val diaryTitle = intent.getStringExtra("DiaryTitle") ?: ""
        val diaryFaceId = intent.getIntExtra("DiaryFaceID", -1)

        if (diaryId != -1) {
            editTextDiaryTitle.setText(diaryTitle)
            editTextDiaryContent.setText(diaryContent)

            findViewById<ImageView>(R.id.face1).alpha = 0.3f
            findViewById<ImageView>(R.id.face2).alpha = 0.3f
            findViewById<ImageView>(R.id.face3).alpha = 0.3f
            findViewById<ImageView>(R.id.face4).alpha = 0.3f

            selectedFaceId = faceDrawableToIdMap[diaryFaceId] ?: return
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

        findViewById<ImageView>(selectedId).alpha = 1.0f
    }

    fun saveDiaryEntry(view: View) {
        val title = editTextDiaryTitle.text.toString().trim()
        val content = editTextDiaryContent.text.toString().trim()

        val existingDiaryId = intent.getIntExtra("DiaryID", -1)

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Title and content cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedFaceId == 0) {
            Toast.makeText(this, "Select one face that represents your mood.", Toast.LENGTH_SHORT).show()
            return
        }

        if (existingDiaryId != -1) {
            val selectedFaceConstant = faceDrawableToIdMap[selectedFaceId] ?: return
            val diaryEntryToUpdate = Diary(existingDiaryId, userId, content, title, getCurrentDate(), selectedFaceConstant)
            val updateSuccess = dbHelper.updateDiaryEntry(diaryEntryToUpdate)

            if (updateSuccess) {
                Toast.makeText(this, "Diary entry updated successfully", Toast.LENGTH_SHORT).show()
                folderIntent()
            } else {
                Toast.makeText(this, "Failed to update diary entry", Toast.LENGTH_SHORT).show()
            }
        } else {
            val titleExists = checkTitleExists(title, userId)
            if (titleExists) {
                Toast.makeText(this, "An entry with this title already exists.", Toast.LENGTH_SHORT).show()
                return
            }

            val selectedFaceConstant = faceDrawableToIdMap[selectedFaceId] ?: return
            val newDiaryEntry = Diary(0, userId, content, title, getCurrentDate(), selectedFaceConstant)
            val insertSuccess = dbHelper.insertDiaryEntry(newDiaryEntry)

            if (insertSuccess) {
                Toast.makeText(this, "Diary entry saved successfully", Toast.LENGTH_SHORT).show()
                folderIntent()
            } else {
                Toast.makeText(this, "Failed to save diary entry", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun burnDiaryEntry(view: View) {
        AlertDialog.Builder(this).apply {
            setTitle("Burn Diary Entry")
            setMessage("Are you sure you want to burn this diary entry? This action cannot be undone.")
            setPositiveButton("Burn") { dialog, which ->
                val diaryId = intent.getIntExtra("DiaryID", -1)
                if (diaryId != -1) {
                    val success = dbHelper.deleteDiaryEntry(diaryId)
                    if (success) {
                        Toast.makeText(context, "Diary entry burned successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to burn diary entry", Toast.LENGTH_SHORT).show()
                    }
                }
                folderIntent()
                finish()
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    private fun checkTitleExists(title: String, userId: Int): Boolean {
        val newcursor: Cursor? = dbHelper.getDiaryEntries()
        while(newcursor!!.moveToNext()) {
            val UserID = newcursor.getInt(1)
            val DiaryTitle = newcursor.getString(3)

            if (title == DiaryTitle && userId == UserID) {
                return true
            }
        }
        newcursor?.close()
        return false
    }

    private fun getCurrentDate(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }
    fun diaryFolderEntities(view: View){
        folderIntent()
    }

    private fun folderIntent() {
        val intent = Intent(this, DiaryFolder::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }
}
