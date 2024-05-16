package com.example.EmpowerTasksAndMood

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty full name return false`() {
        val result = RegistrationUtil.validateRegistrationInput("", "ads@asd.com", 1234567899, "oscar",
            "12345678", "12345678")
        assertThat(result).isFalse()
    }

    @Test
    fun `empty email return false`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "", 1234567899, "oscar",
            "12345678", "12345678")
        assertThat(result).isFalse()
    }


    @Test
    fun `empty username return false`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "ads@asd.com", 1234567899, "",
            "12345678", "12345678")
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password return false`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "ads@asd.com", 1234567899, "oscar",
            "", "12345678")
        assertThat(result).isFalse()
    }

    @Test
    fun `empty check password return false`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "ads@asd.com", 1234567899, "oscar",
            "12345678", "")
        assertThat(result).isFalse()
    }

    @Test
    fun `phone number starts with 0 return false`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "ads@asd.com", 1234567899, "oscar",
            "12345678", "12345678")
        assertThat(result).isFalse()
    }

    @Test
    fun `username is only letters return true`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "ads@asd.com", 1234567899, "oscar",
            "12345678", "asdfghjk")
        assertThat(result).isTrue()
    }

    @Test
    fun `full name contains at least 3 letters returns true`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "ads@asd.com", 1234567899, "oscar",
            "12345678", "12345678")
        assertThat(result).isTrue()
    }

    @Test
    fun `email is valid returns true`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "ads@asd.com", 1234567899, "oscar",
            "12345678", "12345678")
        assertThat(result).isTrue()
    }

    @Test
    fun `phone number has 10 or more digits return true`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "ads@asd.com", 1234567899, "oscar",
            "12345678", "12345678")
        assertThat(result).isTrue()
    }

    @Test
    fun `username at least 3 letters long return true`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "ads@asd.com", 1234567899, "oscar",
            "12345678", "12345678")
        assertThat(result).isTrue()
    }

   @Test
    fun `username already exist returns false`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "ads@asd.com", 1234567899, "Raul",
            "12345678", "12345678")
        assertThat(result).isFalse()
    }

    @Test
    fun `incorrectly confirmed password returns false`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "ads@asd.com", 1234567899, "oscar",
            "12345678", "asdfghjk")
        assertThat(result).isFalse()
    }


/**
    @Test
    fun `EXAMPLE`() {
        val result = RegistrationUtil.validateRegistrationInput("Oscar", "ads@asd.com", 1234567899, "oscar",
            "12345678", "12345678")
        assertThat(result).isTrue()
    }

    */

}
