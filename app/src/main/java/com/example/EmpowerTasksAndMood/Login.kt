package com.example.EmpowerTasksAndMood

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.EmpowerTasksAndMood.Model.DBHelper

class Login : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    val dbHelper: DBHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("com.example.EmpowerTasksAndMood.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)

        attemptAutoLogin()
    }

    private fun attemptAutoLogin() {
        val savedUsername = sharedPreferences.getString("username", null)
        val savedPassword = sharedPreferences.getString("password", null)
        if (savedUsername != null && savedPassword != null) {
            val userFromDb = dbHelper.getUserByUsername(savedUsername)
            val adminFromDb = dbHelper.getAdminByUsername(savedUsername)

            when {
                userFromDb != null && userFromDb.UserPassword == savedPassword -> {
                    MainActivity(userFromDb.UserId)
                }
                adminFromDb != null && adminFromDb.AdminPassword == savedPassword -> {
                    MainActivityAdmin(adminFromDb.AdminId)
                }
            }
        }
    }

    fun login(view: View) {
        val password = findViewById<EditText>(R.id.editTextLoginPassword).text.toString()
        val username = findViewById<EditText>(R.id.editTextLoginUsername).text.toString()
        val message = findViewById<TextView>(R.id.textViewLoginMessage)

        if (username.isEmpty() || password.isEmpty()) {
            message.text = "Please insert Username and Password"
            return
        }

        val adminId = dbHelper.getAdminId(username)
        val adminFromDb = if (adminId != -1) dbHelper.getAdmin(adminId) else null

        if (adminFromDb != null && adminFromDb.AdminPassword == password) {
            saveDetails(username, password)
            MainActivityAdmin(adminId)
            return
        }

        val userId = dbHelper.getUserId(username)
        val userFromDb = if (userId != -1) dbHelper.getUser(userId) else null

        if (userFromDb != null && userFromDb.UserPassword == password) {
            saveDetails(username, password)
            MainActivity(userId)
        } else {
            message.text = "Invalid username or password"
        }
    }

    private fun saveDetails(username: String, password: String) {
        if (findViewById<CheckBox>(R.id.checkBoxRememberMe).isChecked) {
            sharedPreferences.edit().apply {
                putString("username", username)
                putString("password", password)
                apply()
            }
        } else {
            sharedPreferences.edit().apply {
                remove("username")
                remove("password")
                apply()
            }
        }

    }

    private fun MainActivity(userId: Int) {
        val intent = Intent (this, MainActivity::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

    private fun MainActivityAdmin(adminId: Int) {
        val intent = Intent (this, Admin::class.java)
        intent.putExtra("admin_ID", adminId)
        startActivity(intent)
    }

    fun registerPage(view: View){
        val intent = Intent (this, Register::class.java)
        startActivity(intent)
    }
}
