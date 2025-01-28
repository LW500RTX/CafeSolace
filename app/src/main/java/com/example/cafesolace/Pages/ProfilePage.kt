package com.example.cafesolace.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import com.example.cafesolace.R

@Composable
fun ProfilePage() {
    var username by remember { mutableStateOf("Aloka Silva") }
    var email by remember { mutableStateOf("Alokasilva@gmail.com") }
    var password by remember { mutableStateOf("password123") }
    var contact by remember { mutableStateOf("123-456-7890") }
    var isEditable by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Profile Image
        Image(
            painter = painterResource(id = R.drawable.man),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = username,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(26.dp))

        // Card with user details
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceBright)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // Username row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Username:", style = MaterialTheme.typography.bodyLarge)
                    if (isEditable) {
                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            modifier = Modifier.width(200.dp)
                        )
                    } else {
                        Text(text = username, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Email row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Email:", style = MaterialTheme.typography.bodyLarge)
                    if (isEditable) {
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.width(200.dp)
                        )
                    } else {
                        Text(text = email, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Password row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Password:", style = MaterialTheme.typography.bodyLarge)
                    if (isEditable) {
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.width(200.dp)
                        )
                    } else {
                        Text(text = "********", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Contact row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Contact:", style = MaterialTheme.typography.bodyLarge)
                    if (isEditable) {
                        TextField(
                            value = contact,
                            onValueChange = { contact = it },
                            label = { Text("Contact") },
                            modifier = Modifier.width(200.dp)
                        )
                    } else {
                        Text(text = contact, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Edit/Save Button
        Button(
            onClick = {
                isEditable = !isEditable
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.scrim,
                contentColor = Color.White
            )
        ) {
            Text(text = if (isEditable) "Save" else "Edit")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        Button(
            onClick = {
                // Handle logout logic here
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
//                contentColor = Color.White
            )
        ) {
            Text(text = "Logout")
        }
        Spacer(modifier = Modifier.height(90.dp))
    }

}


//@Preview(showBackground = true)
//@Composable
//fun ProfilePagePreview() {
//    ProfilePage()
//}
