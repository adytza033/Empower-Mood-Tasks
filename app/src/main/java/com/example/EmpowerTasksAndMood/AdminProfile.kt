package com.example.EmpowerTasksAndMood

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Admin


class AdminProfile : AppCompatActivity() {
    val dbHelper: DBHelper = DBHelper(this)
    var adminId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile)

        adminId = intent.getIntExtra("admin_ID", 0)
        details()
    }

    fun details() {
        val adminFromDb = dbHelper.getAdmin(adminId)
        val adminFullName = findViewById<EditText>(R.id.editTextAdminFullName)
        val adminEmail = findViewById<EditText>(R.id.editTextAdminEmail)
        val adminPhone = findViewById<EditText>(R.id.editTextAdminPhoneNumber)
        val adminUsername = findViewById<EditText>(R.id.editTextAdminUsername)

        adminFullName.setText(adminFromDb.AdminFullName)
        adminEmail.setText(adminFromDb.AdminEmail)
        adminPhone.setText(adminFromDb.AdminPhoneNo)
        adminUsername.setText(adminFromDb.AdminUserName)
    }


    fun updateAdmin(view: View) {
        val adminFromDb = dbHelper.getAdmin(adminId)
        val adminEmail = findViewById<EditText>(R.id.editTextAdminEmail).text.toString()
        val adminPhone = findViewById<EditText>(R.id.editTextAdminPhoneNumber).text.toString()
        val adminPassword = findViewById<EditText>(R.id.editTextAdminPassword).text.toString()
        val message = findViewById<TextView>(R.id.textViewUpdateMessage)

        if (!isEmail(adminEmail)) {
            message.text = "You must enter a correct email."
        }
        else if (!isPhoneNo(adminPhone)) {
            message.text = "You must enter a correct phone number."
        }
        else if (!checkPassword(adminPassword)) {
            message.text = "Password must be correct"
        }
        else {

            if (adminFromDb != null) {
                val adminToUpdate = Admin(adminFromDb.AdminId, adminFromDb.AdminFullName, adminEmail, adminPhone, adminFromDb.AdminUserName, adminPassword)

                val updateSuccessful = dbHelper.updateAdmin(adminToUpdate)
                if (updateSuccessful) {
                    Toast.makeText(this, "Information updated successfully.", Toast.LENGTH_SHORT).show()
                    message.text = ""
                } else {
                    Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    fun updatePassword(view: View) {
        val adminFromDb = dbHelper.getAdmin(adminId)
        val oldPassword = findViewById<EditText>(R.id.editTextOldPassword).text.toString()
        val newPassword = findViewById<EditText>(R.id.editTextNewPassword).text.toString()
        val repeatPassword = findViewById<EditText>(R.id.editTextRepeatPassword).text.toString()

        if (adminFromDb.AdminPassword != oldPassword) {
            Toast.makeText(this, "Old password is incorrect.", Toast.LENGTH_SHORT).show()
            return
        }
        if (!checkPassword(newPassword)) {
            Toast.makeText(this, "New password needs to be at least 8 letters long, 1 capital letter and 1 symbol or 1 number.", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword != repeatPassword) {
            Toast.makeText(this, "New passwords do not match.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedAdmin = adminFromDb.copy(AdminPassword = newPassword)

        val updateSuccessful = dbHelper.updateAdmin(updatedAdmin)

        if (updateSuccessful) {
            Toast.makeText(this, "Password updated successfully.", Toast.LENGTH_SHORT).show()
            setContentView(R.layout.activity_profile)
        } else {
            Toast.makeText(this, "Failed to update password.", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteAccount (view: View) {
        val adminFromDb = dbHelper.getAdmin(adminId)
        if (dbHelper.deleteAdmin(adminFromDb)) {
            Toast.makeText(this, "Account deleted successfully.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        else {
            Toast.makeText(this, "Failed to delete account.", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteConfirmation (view: View) {
        val adminFromDb = dbHelper.getAdmin(adminId)
        val adminUsername = findViewById<EditText>(R.id.editTextAdminUsername).text.toString()
        val adminPassword = findViewById<EditText>(R.id.editTextAdminPassword).text.toString()

        if (adminFromDb.AdminUserName == adminUsername && adminFromDb.AdminPassword == adminPassword) {
            setContentView(R.layout.activity_admin_delete)
        } else {
            Toast.makeText(this, "Above password must be correct to continue", Toast.LENGTH_SHORT).show()
        }
    }

    fun isEmail(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
        return regex.matches(email)
    }

    fun checkPassword(password: String): Boolean {
        if (password.length < 8) {
            return false
        }
        if (!password.any { it.isUpperCase() }) {
            return false
        }
        if (!password.any { it.isDigit() || it.isLetter() && !it.isLetterOrDigit() }) {
            return false
        }
        return true
    }

    fun isPhoneNo(phoneNo: String): Boolean {
        val regex = Regex("^\\+?[0-9-]+\$")
        return regex.matches(phoneNo) && phoneNo.length >= 10
    }

    fun profileAdmin(view: View){
        setContentView(R.layout.activity_admin_profile)
        details()
    }
    fun mainPageAdmin(view: View){
        val intent = Intent(this, com.example.EmpowerTasksAndMood.Admin::class.java)
        intent.putExtra("admin_ID", adminId)
        startActivity(intent)
    }

    fun changeAdminPassword(view: View){
        setContentView(R.layout.activity_admin_change_password)
    }
}