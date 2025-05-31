package com.example.cafesolace.Authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

// ViewModel responsible for managing user authentication state using FirebaseAuth
class AuthViewModel1 : ViewModel() {

    // FirebaseAuth instance to handle authentication operations
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Internal MutableLiveData to hold current authentication state
    private val _authState = MutableLiveData<AuthState>()

    // Public immutable LiveData to expose authentication state to UI observers
    val authState: LiveData<AuthState> = _authState

    // Initialize by checking the current authentication state
    init {
        checkAuthState()
    }

    // Check if there is a currently logged-in user and update auth state accordingly
    fun checkAuthState() {
        if (auth.currentUser == null) {
            // No user logged in
            _authState.value = AuthState.Unauthenticated
        } else {
            // User is logged in
            _authState.value = AuthState.Authenticated
        }
    }

    // Login user with email and password
    fun login(email: String, password: String) {
        // Validate that email and password are not empty
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password can't be empty")
            return
        }

        // Update state to Loading while login is in progress
        _authState.value = AuthState.Loading

        // Attempt to sign in using FirebaseAuth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful, update state to Authenticated
                    _authState.value = AuthState.Authenticated
                } else {
                    // Login failed, update state with error message
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    // Register a new user with email and password
    fun signup(email: String, password: String) {
        // Validate that email and password are not empty
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password can't be empty")
            return
        }

        // Update state to Loading while signup is in progress
        _authState.value = AuthState.Loading

        // Attempt to create a new user using FirebaseAuth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Signup successful, update state to Authenticated
                    _authState.value = AuthState.Authenticated
                } else {
                    // Signup failed, update state with error message
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    // Sign out the current user
    fun signout() {
        auth.signOut()
        // Update state to Unauthenticated after sign out
        _authState.value = AuthState.Unauthenticated
    }
}

// Sealed class representing different states of authentication
sealed class AuthState {
    object Authenticated : AuthState()        // User is authenticated
    object Unauthenticated : AuthState()      // User is not authenticated
    object Loading : AuthState()               // Authentication operation in progress
    data class Error(val message: String) : AuthState()  // Authentication error with message
}
