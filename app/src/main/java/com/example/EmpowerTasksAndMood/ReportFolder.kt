package com.example.EmpowerTasksAndMood

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Adapters.ReportAdapter
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Report
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportFolder : AppCompatActivity() {
    private lateinit var recyclerViewReports: RecyclerView
    private lateinit var reportsList: ArrayList<Report>

    val dbHelper: DBHelper = DBHelper(this)
    var userId: Int = 0
    var adminId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userId = intent.getIntExtra("user_ID", 0)
        adminId = intent.getIntExtra("admin_ID", 0)

        if(adminId == 0) {
            setContentView(R.layout.activity_report_folder)
        } else {
            setContentView(R.layout.activity_admin_report_folder)
        }

        recyclerViewReports = findViewById(R.id.recycleViewReports)
        recyclerViewReports.layoutManager = LinearLayoutManager(this)

        displayReports()
    }

    private fun displayReports() {
        val cursor: Cursor? = dbHelper.getAllReports()
        reportsList = ArrayList<Report>()
        if (adminId == 0) {
            while (cursor!!.moveToNext()) {
                val reportId = cursor.getInt(0)
                val userID = cursor.getInt(1)
                val adminID = cursor.getInt(2)
                val title = cursor.getString(3)
                val description = cursor.getString(4)
                val date = formatDiaryEntryDate(cursor.getString(5).toInt())
                if (userID == userId) {
                    reportsList.add(Report(reportId, userID, adminID, title, description, date))
                }
            }
            cursor.close()

            reportsList.sortWith(compareByDescending { it.ReportID })
            recyclerViewReports.adapter = ReportAdapter(reportsList, dbHelper, 0)
        } else {
            while (cursor!!.moveToNext()) {
                val reportId = cursor.getInt(0)
                val userID = cursor.getInt(1)
                val adminID = cursor.getInt(2)
                val title = cursor.getString(3)
                val description = cursor.getString(4)
                val date = formatDiaryEntryDate(cursor.getString(5).toInt())

                reportsList.add(Report(reportId, userID, adminID, title, description, date))
            }
            cursor.close()

            reportsList.sortWith(compareByDescending { it.ReportID })
            recyclerViewReports.adapter = ReportAdapter(reportsList, dbHelper, adminId)
        }

    }

    private fun formatDiaryEntryDate(timestamp: Int): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp * 1000L))
    }

    fun menu(view: View){
        val intent = Intent(this, Menu::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

    fun adminPage(view: View){
        val intent = Intent(this, Admin::class.java)
        intent.putExtra("admin_ID", adminId)
        startActivity(intent)
    }

    fun createReport(view: View){
        val intent = Intent(this, com.example.EmpowerTasksAndMood.Report::class.java)
        intent.putExtra("user_ID", userId)
        intent.putExtra("admin_ID", adminId)
        startActivity(intent)
    }
}
