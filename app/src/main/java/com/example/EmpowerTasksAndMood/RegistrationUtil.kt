package com.example.EmpowerTasksAndMood

object RegistrationUtil {

    private val dbHelper = listOf("Peter", "Raul")
    /**
     * the input is not valid if
     * the input is empty
     * the username is taken
     * then confirm password is not the same as the real password
     * the password contains less then 8 digits
     */

    fun validateRegistrationInput(fullName: String, email: String, phoneNumber: Int, username: String, password: String, confirmPassword:String): Boolean {
        if(fullName.isEmpty() || email.isEmpty() || phoneNumber.toString().isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            return false
            }
        if(fullName.length < 4 || email.length < 4 || phoneNumber.toString().length < 10 || username.length < 4 ||
                password.length < 4 || confirmPassword.length < 4) {
            return false
        }
        if(fullName.firstOrNull{!it.isLetter()} == null){
            return true
        }
        if(!phoneNumber.toString().startsWith("0")){
            return false
        }
        if(username in dbHelper){
            return false
        }
        if(!checkPassword(password)) {
            return false
        }
        if(password != confirmPassword){
            return false
        }
        if(!isEmail(email) ){
            return false
        }

        return true
    }

    //function to check the password
    fun checkPassword(password: String): Boolean {
        if (password.length < 8){
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

    //funtion to check for email address
    fun isEmail(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
        return regex.matches(email)
    }

    fun isNumericToX(toCheck: String): Boolean {
        return toCheck.toDoubleOrNull() != null
    }


}

