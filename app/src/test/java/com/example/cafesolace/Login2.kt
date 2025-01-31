package com.example.cafesolace


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

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
                    if (email.text.isEmpty()) {
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
                    if (password.text.isEmpty()) {
                        Text("Password", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Handle Login logic */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { /* Navigate to SignUp Screen */ }) {
            Text("Don't have an account? Sign Up")
        }
    }
}


