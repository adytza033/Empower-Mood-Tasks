package com.example.EmpowerTasksAndMood

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Report
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Report : AppCompatActivity() {
    val dbHelper: DBHelper = DBHelper(this)
    var userId: Int = 0
    var adminId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userId = intent.getIntExtra("user_ID", 0)
        adminId = intent.getIntExtra("admin_ID", 0)

        if(adminId == 0){
            setContentView(R.layout.activity_report)
        } else {
            setContentView(R.layout.activity_admin_report)
        }

        if (intent.hasExtra("ReportID")) {
            populateReportDetails()
        }

    }

    private fun populateReportDetails() {
        val reportId = intent.getIntExtra("ReportID", -1)
        val report = dbHelper.getReport(reportId)
        val reportSentBy =  dbHelper.getUser(report.UserID).UserEmail
        if (reportId != -1) {
            if(adminId == 0) {
                val title = findViewById<EditText>(R.id.reportTitle)
                val content = findViewById<EditText>(R.id.reportContent)
                title.setText(report.ReportTitle)
                content.setText(report.ReportDescription)
                title.isEnabled = false
                content.isEnabled = false

            } else {
                val sentBy = findViewById<EditText>(R.id.reportUser)
                val title = findViewById<EditText>(R.id.reportTitleAdmin)
                val content = findViewById<EditText>(R.id.reportContentAdmin)
                sentBy.setText(reportSentBy)
                title.setText(report.ReportTitle)
                content.setText(report.ReportDescription)
                sentBy.isEnabled = false
                title.isEnabled = false
            }
        }
    }


    fun submitReportUser(view: View) {
        val reportTitle = findViewById<EditText>(R.id.reportTitle).text.toString()
        val reportDescription = findViewById<EditText>(R.id.reportContent).text.toString()
        val date = getCurrentDate().toString()
        val reportId = intent.getIntExtra("ReportID", -1)

        val newReport = Report(0, userId, 0, reportTitle, reportDescription, date)
        val existingReport = if (reportId != -1) dbHelper.getReport(reportId) else null
        if (existingReport == null) {
            if (!reportTitle.isEmpty() && !reportDescription.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
            val result = dbHelper.addReport(newReport)

            if (result != -1L) {
                Toast.makeText(this, "Report added successfully!", Toast.LENGTH_SHORT).show()
                folder()
            } else {
                Toast.makeText(this, "Failed to add report.", Toast.LENGTH_SHORT).show()
            }
        } else {
            finish()
        }
    }

    fun updateReport() {
        val reportDescription = findViewById<EditText>(R.id.reportContentAdmin).text.toString()
        val reportId = intent.getIntExtra("ReportID", -1)

        if (reportId != -1) {
            val success = dbHelper.updateReport(reportId, reportDescription)
            if (success) {
                Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Email did not send.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun submitReportAdmin(view: View) {
        val reportTitle = findViewById<EditText>(R.id.reportTitleAdmin).text.toString()
        val reportDescription = findViewById<EditText>(R.id.reportContentAdmin).text.toString()
        val userEmail = findViewById<EditText>(R.id.reportUser).text.toString()
        val date = getCurrentDate().toString()
        val userID = dbHelper.getUserByEmail(userEmail).UserId
        val reportId = intent.getIntExtra("ReportID", -1)

        if (reportTitle.isEmpty() && reportDescription.isEmpty() && userEmail.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val existingReport = if (reportId != -1) dbHelper.getReport(reportId) else null
        if (existingReport != null) {
            updateReport()
        } else {
            val newReport = Report(0, userID, adminId, reportTitle, reportDescription, date)

            val result = dbHelper.addReport(newReport)

            if (result != -1L) {
                Toast.makeText(this, "Report added successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to add report.", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun getCurrentDate(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }

    private fun formatDiaryEntryDate(timestamp: Int): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp * 1000L))
    }

    fun reportFolder(view: View){
        folder()
    }

    private fun folder() {
        val intent = Intent(this, ReportFolder::class.java)
        intent.putExtra("user_ID", userId)
        intent.putExtra("admin_ID", adminId)
        startActivity(intent)
    }

    fun menu(view: View){
        val intent = Intent(this, Menu::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }
}