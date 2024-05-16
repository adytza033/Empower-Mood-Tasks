package com.example.EmpowerTasksAndMood

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.User

class Admin : AppCompatActivity() {
    val dbHelper: DBHelper = DBHelper(this)
    var adminId: Int = 0

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        adminId = intent.getIntExtra("admin_ID", 0)

        sharedPreferences = getSharedPreferences("com.example.EmpowerTasksAndMood.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)

        helloName()
    }

    fun helloName() {
        val helloName = findViewById<TextView>(R.id.Hello)
        val adminFromDb = dbHelper.getAdmin(adminId)
        val firstName = adminFromDb.AdminFullName.split(" ").firstOrNull()

        helloName.text = "Hello $firstName"
    }

    fun getUserdetails(view: View) {
        val userEmailFind = findViewById<EditText>(R.id.editTextUserEmailFind).text.toString()
        val userFullName = findViewById<EditText>(R.id.editTextUserFullName)
        val userEmail = findViewById<EditText>(R.id.editTextUserEmail)
        val userPhone = findViewById<EditText>(R.id.editTextUserPhoneNumber)
        val userUsername = findViewById<EditText>(R.id.editTextUserUsername)

        val user = dbHelper.getUserByEmail(userEmailFind)

        userFullName.setText(user.UserFullName)
        userEmail.setText(user.UserEmail)
        userPhone.setText(user.UserPhoneNo)
        userUsername.setText(user.UserUserName)
    }

    fun updateUser(view: View) {
        val userEmailFind = findViewById<EditText>(R.id.editTextUserEmailFind).text.toString()
        val userFullName = findViewById<EditText>(R.id.editTextUserFullName).text.toString()
        val userEmail = findViewById<EditText>(R.id.editTextUserEmail).text.toString()
        val userPhone = findViewById<EditText>(R.id.editTextUserPhoneNumber).text.toString()
        val userUsername = findViewById<EditText>(R.id.editTextUserUsername).text.toString()
        val message = findViewById<TextView>(R.id.textViewUpdateMessage)

        val userFromDb = dbHelper.getUserByEmail(userEmailFind)

        if (userFullName.isEmpty()) {
            message.text = "You must enter your full name."
            return
        } else if (!isEmail(userEmail)) {
            message.text = "You must enter a correct email."
            return
        } else if (!isPhoneNo(userPhone)) {
            message.text = "You must enter a correct phone number."
            return
        }

        val existingUserWithUsername = dbHelper.getUserByUsername(userUsername)
        val existingUserWithEmail = dbHelper.getUserByEmail(userEmail)

        when {
            existingUserWithUsername != null && existingUserWithUsername.UserId != userFromDb?.UserId -> {
                message.text = "Username already exists."
            }
            existingUserWithEmail != null && existingUserWithEmail.UserId != userFromDb?.UserId -> {
                message.text = "Email already exists."
            }
            userFromDb != null -> {
                val userToUpdate = User(userFromDb.UserId, userFullName, userEmail, userPhone, userUsername, userFromDb.UserPassword, userFromDb.UserIsActive)

                val updateSuccessful = dbHelper.updateUser(userToUpdate)
                if (updateSuccessful) {
                    Toast.makeText(this, "Information updated successfully.", Toast.LENGTH_SHORT).show()
                    message.text = ""
                } else {
                    Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> message.text = "User not found."
        }
    }

    fun updatePassword(view: View) {
        val userEmailChangePasswordEditText = findViewById<EditText>(R.id.editTextUserEmailChangePassword).text.toString()
        val newPassword = findViewById<EditText>(R.id.editTextNewPassword).text.toString()
        val repeatPassword = findViewById<EditText>(R.id.editTextRepeatPassword).text.toString()
        val user = dbHelper.getUserByEmail(userEmailChangePasswordEditText)

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

    fun changeUserPassword(view: View) {
        val userEmailFind = findViewById<EditText>(R.id.editTextUserEmailFind).text.toString()
        val userEmail = findViewById<EditText>(R.id.editTextUserEmail).text.toString()
        if(userEmailFind.isEmpty()){
            Toast.makeText(this, "User email is missing", Toast.LENGTH_SHORT).show()
        } else if(userEmailFind != userEmail) {
            Toast.makeText(this, "User email is not the same", Toast.LENGTH_SHORT).show()
        }
        else {
            setContentView(R.layout.activity_admin_change_user_password)

            val userEmailChangePasswordEditText = findViewById<EditText>(R.id.editTextUserEmailChangePassword)
            userEmailChangePasswordEditText.setText(userEmail)
        }
    }

    fun deleteAccount(view: View) {
        val userEmailDeleteAccount = findViewById<EditText>(R.id.editTextUserEmailDeleteAccount).text.toString()
        val user = dbHelper.getUserByEmail(userEmailDeleteAccount)

        if (dbHelper.deleteUser(user)) {
            Toast.makeText(this, "Account deleted successfully.", Toast.LENGTH_SHORT).show()
            setContentView(R.layout.activity_get_user)
        } else {
            Toast.makeText(this, "Failed to delete account.", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteUserAccount(view: View) {
        val userEmailFind = findViewById<EditText>(R.id.editTextUserEmailFind).text.toString()
        val userEmail = findViewById<EditText>(R.id.editTextUserEmail).text.toString()

        if (userEmailFind.isEmpty()) {
            Toast.makeText(this, "User email is missing", Toast.LENGTH_SHORT).show()
        } else if (userEmailFind != userEmail) {
            Toast.makeText(this, "User email is not the same", Toast.LENGTH_SHORT).show()
        } else {
            setContentView(R.layout.activity_delete_user)

            val userEmailDeleteAccount = findViewById<EditText>(R.id.editTextUserEmailDeleteAccount)
            userEmailDeleteAccount.setText(userEmail)
        }
    }

    fun loginPage(view:View){
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.remove("password")
        editor.apply()

        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    fun adminProfile(view: View) {
        val intent = Intent(this, AdminProfile::class.java)
        intent.putExtra("admin_ID", adminId)
        startActivity(intent)
    }

    fun getUserdetailsForm(view: View){
        setContentView(R.layout.activity_get_user)
    }

    fun mainPageAdmin(view: View){
        setContentView(R.layout.activity_admin)
    }

    fun quotes(view: View) {
        val intent = Intent(this, Quotes::class.java)
        intent.putExtra("admin_ID", adminId)
        startActivity(intent)
    }

    fun reportFolder(view: View) {
        val intent = Intent(this, ReportFolder::class.java)
        intent.putExtra("admin_ID", adminId)
        startActivity(intent)
    }

}