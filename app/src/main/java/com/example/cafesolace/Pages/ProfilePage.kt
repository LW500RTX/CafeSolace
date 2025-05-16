package com.example.cafesolace.Pages

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.cafesolace.Authentication.AuthState
import com.example.cafesolace.Authentication.AuthViewModel1
import com.example.cafesolace.CommonSection.ConnectivityObserver
import com.example.cafesolace.CommonSection.ConnectionStatus
import com.example.cafesolace.R

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel1: AuthViewModel1
) {
    var username by remember { mutableStateOf("Lalan Weerasooriya") }
    var email by remember { mutableStateOf("lalanweerasooriya@gmail.com") }
    var password by remember { mutableStateOf("password123") }
    var contact by remember { mutableStateOf("123-456-7890") }
    var isEditable by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val authState = authViewModel1.authState.observeAsState()

    // Camera image URI holder
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { imageUri = it }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = cameraImageUri.value
        }
    }

    fun createImageUri(): Uri? {
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "profile_image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    // Connectivity observer setup
    val connectivityObserver = remember { ConnectivityObserver(context) }
    val connectionStatus by connectivityObserver.connectionStatus.collectAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            connectivityObserver.unregisterCallback()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Profile Image
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clickable { showDialog = true }
        ) {
            Image(
                painter = if (imageUri != null) rememberAsyncImagePainter(imageUri)
                else painterResource(id = R.drawable.man), // Default image
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        // Image Picker Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        text = "Select Image Source",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                showDialog = false
                                val uri = createImageUri()
                                cameraImageUri.value = uri
                                uri?.let { cameraLauncher.launch(it) }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text("Camera")
                        }

                        Button(
                            onClick = {
                                showDialog = false
                                galleryLauncher.launch("image/*")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text("Gallery")
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {}
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Internet connectivity status
        Text(
            text = "Internet  Connection: ${connectionStatus.name}",
            style = MaterialTheme.typography.bodyLarge,
            color = when (connectionStatus) {
                ConnectionStatus.Available -> Color.Green
                ConnectionStatus.Losing -> Color.Yellow
                ConnectionStatus.Lost, ConnectionStatus.Unavailable -> Color.Red
            },
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Username
        Text(text = username, style = MaterialTheme.typography.headlineSmall)
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
                authViewModel1.signout()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
            )
        ) {
            Text(text = "Logout")
        }

        Spacer(modifier = Modifier.height(90.dp))
    }
}
