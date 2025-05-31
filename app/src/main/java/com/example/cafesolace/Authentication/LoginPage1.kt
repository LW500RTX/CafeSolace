package com.example.cafesolace.Authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cafesolace.R

@Composable
fun LoginPage1(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel1: AuthViewModel1
) {
    // State variables to hold user input and password visibility toggle
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Observe the authentication state from the ViewModel as Compose State
    val authState = authViewModel1.authState.observeAsState()

    // Get the current Android context to show Toast messages
    val context = LocalContext.current

    // Side-effect to react to authentication state changes
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated ->
                // Navigate to "home" screen upon successful login
                navController.navigate("home")

            is AuthState.Error ->
                // Show error message as a Toast if login fails
                Toast.makeText(
                    context,
                    (authState.value as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()

            else -> Unit // Do nothing for other states (Unauthenticated, Loading)
        }
    }

    // Outer container with vertical gradient background and centered content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2196F3), Color(0xFFBBDEFB))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Card container for the login form with padding, rounded corners and elevation
        Card(
            modifier = Modifier.padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            // Column inside card to layout the UI elements vertically and centered
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Decorative image at the top of the login card
                Image(
                    painter = painterResource(id = R.drawable.banner),
                    contentDescription = "Login Image",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 12.dp)
                )

                // Title text
                Text(text = "Login", fontSize = 32.sp, color = Color(0xFF333333))
                Spacer(modifier = Modifier.height(16.dp))

                // Email input field with label and single line support
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Password input field with toggleable visibility and label
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Login button triggers login function in the ViewModel
                Button(
                    onClick = { authViewModel1.login(email, password) },
                    enabled = authState.value != AuthState.Loading, // Disable button while loading
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Login", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Text button for navigation to the signup screen
                TextButton(onClick = { navController.navigate("signup") }) {
                    Text(text = "Don't have an account? Sign up", color = Color(0xFF1976D2))
                }
            }
        }
    }
}
