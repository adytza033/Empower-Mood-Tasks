package com.example.EmpowerTasksAndMood

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.User

class Update_Delete: AppCompatActivity() {

    val dbHelper: DBHelper = DBHelper(this)
    var userId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        userId = intent.getIntExtra("user_ID", 0)
        details()

    }
    fun details() {
        val user = dbHelper.getUser(userId)
        val userFullName = findViewById<EditText>(R.id.editTextUserFullName)
        val userEmail = findViewById<EditText>(R.id.editTextUserEmail)
        val userPhone = findViewById<EditText>(R.id.editTextUserPhoneNumber)
        val userUsername = findViewById<EditText>(R.id.editTextUserUsername)

        userFullName.setText(user?.UserFullName)
        userEmail.setText(user?.UserEmail)
        userPhone.setText(user?.UserPhoneNo)
        userUsername.setText(user?.UserUserName)
    }

    fun updateUser(view: View) {
        val userFromDb = dbHelper.getUser(userId)
        val userFullName = findViewById<EditText>(R.id.editTextUserFullName).text.toString()
        val userEmail = findViewById<EditText>(R.id.editTextUserEmail).text.toString()
        val userPhone = findViewById<EditText>(R.id.editTextUserPhoneNumber).text.toString()
        val userUsername = findViewById<EditText>(R.id.editTextUserUsername).text.toString()
        val userPassword = findViewById<EditText>(R.id.editTextUserPassword).text.toString()
        val message = findViewById<TextView>(R.id.textViewUpdateMessage)

        if (userFullName == "") {
            message.text = "You must enter your full name."
        }
        else if (!isEmail(userEmail)) {
            message.text = "You must enter a correct email."
        }
        else if (!isPhoneNo(userPhone)) {
            message.text = "You must enter a correct phone number."
        }
        else if (!checkPassword(userPassword)) {
            message.text = "Password must be correct"
        }
        else {

            if (userFromDb.UserUserName == userUsername && userFromDb.UserPassword == userPassword) {
                val userToUpdate = User(userFromDb.UserId, userFullName, userEmail, userPhone, userUsername, userPassword, userFromDb.UserIsActive)

                val updateSuccessful = dbHelper.updateUser(userToUpdate)
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
        val user = dbHelper.getUser(userId)
        val oldPassword = findViewById<EditText>(R.id.editTextOldPassword).text.toString()
        val newPassword = findViewById<EditText>(R.id.editTextNewPassword).text.toString()
        val repeatPassword = findViewById<EditText>(R.id.editTextRepeatPassword).text.toString()

        if (user.UserPassword != oldPassword) {
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

        val updatedUser = user.copy(UserPassword = newPassword)
        val updateSuccessful = dbHelper.updateUser(updatedUser)

        if (updateSuccessful) {
            Toast.makeText(this, "Password updated successfully.", Toast.LENGTH_SHORT).show()
            setContentView(R.layout.activity_profile)
        } else {
            Toast.makeText(this, "Failed to update password.", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteAccount(view: View) {
        val user = dbHelper.getUser(userId)
        if (dbHelper.deleteUser(user)) {
            Toast.makeText(this, "Account deleted successfully.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Failed to delete account.", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteConfirmation(view: View) {
        val userFromDb = dbHelper.getUser(userId)
        val userUsername = findViewById<EditText>(R.id.editTextUserUsername).text.toString()
        val userPassword = findViewById<EditText>(R.id.editTextUserPassword).text.toString()

        if (userFromDb.UserUserName == userUsername && userFromDb.UserPassword == userPassword) {
            setContentView(R.layout.activity_delete)
        } else {
            Toast.makeText(this, "Above password must be correct to continue", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun profile(view: View) {
        setContentView(R.layout.activity_profile)
        details()
    }

    fun changePassword(view: View) {
        setContentView(R.layout.activity_change_password)
    }


    fun menuPage(view: View) {
        val intent = Intent(this, Menu::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
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
}