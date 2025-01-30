package com.example.cafesolace.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val validEmail = "test@gmail.com"
    private val validPassword = "12345678"

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            delay(1000) // Simulate network delay

            if (email == validEmail && password == validPassword) {
                onResult(true) // Successful login
            } else {
                onResult(false) // Login failed
            }
        }
    }
}
