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

class Register : AppCompatActivity() {

    val dbHelper: DBHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

    }

    fun signup(view: View) {
        val fullName = findViewById<EditText>(R.id.editTextRegisterFullName).text.toString()
        val email = findViewById<EditText>(R.id.editTextRegisterEmail).text.toString()
        val phoneNo = findViewById<EditText>(R.id.editTextRegisterPhoneNumber).text.toString()
        val username = findViewById<EditText>(R.id.editTextRegisterUsername).text.toString()
        val password = findViewById<EditText>(R.id.editTextRegisterPassword).text.toString()
        val message = findViewById<TextView>(R.id.textViewRegisterMessage)

        val user = User(1, fullName, email, phoneNo, username, password, true)

        if (fullName.length < 3) {
            message.text = "You must enter your full name."
        } else if (!isEmail(email)) {
            message.text = "You must enter a correct email."
            return
        } else if (!isPhoneNo(phoneNo)) {
            message.text = "You must enter a correct phone number."
            return
        } else if ((username.length < 3) || (username.length > 20)) {
            message.text =
                "User name needs to be at least 3 letters long and less than 20 letters total."
        } else if (dbHelper.checkUsernameExists(username)) {
            message.text = "Username already exists. Please choose a different username."
            return
        } else if (checkPassword(password) == false) {
            message.text =
                "Password needs to be at least 8 letters long, 1 capital letter and 1 symbol or 1 number"
        }  else if (dbHelper.checkEmailExists(email)) {
            message.text = "Email already exists. Please use a different email."
            return
        } else if (dbHelper.addUser(user)) {
            Toast.makeText(this, "Your account has been created successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        } else
            Toast.makeText(this, "Error: Account not created", Toast.LENGTH_SHORT).show()
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

    fun loginPage(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}