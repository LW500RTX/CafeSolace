package com.example.cafesolace

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        BasicTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.Gray),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    if (email.isEmpty()) {
                        Text("Email", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.Gray),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    if (password.isEmpty()) {
                        Text("Password", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.Gray),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    if (confirmPassword.isEmpty()) {
                        Text("Confirm Password", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Implement sign-up logic */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            navController.popBackStack() // Navigate back to login screen
        }) {
            Text("Already have an account? Log In")
        }
    }
}

