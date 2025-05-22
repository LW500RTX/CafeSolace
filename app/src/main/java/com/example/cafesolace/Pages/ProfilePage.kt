package com.example.cafesolace.Screens

import android.content.Context
import android.graphics.Color as AndroidColor
import android.net.Uri
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.cafesolace.Authentication.AuthViewModel1
import com.example.cafesolace.CommonSection.ConnectivityObserver
import com.example.cafesolace.CommonSection.ConnectionStatus
import com.example.cafesolace.Data.ProfileDataStore
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest

@Composable
fun ProfilePage(authViewModel1: AuthViewModel1, navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val profileDataStore = remember { ProfileDataStore(context) }

    // Connectivity observer to monitor internet connection
    val connectivityObserver = remember { ConnectivityObserver(context) }
    val connectionStatus by connectivityObserver.connectionStatus.collectAsState()

    val storedUsername by profileDataStore.getUsername.collectAsState(initial = "")
    val storedEmail by profileDataStore.getEmail.collectAsState(initial = "")
    val storedPassword by profileDataStore.getPassword.collectAsState(initial = "")
    val storedContact by profileDataStore.getContact.collectAsState(initial = "")
    val storedImageUriString by profileDataStore.getImageUri.collectAsState(initial = "")

    var username by remember { mutableStateOf(storedUsername) }
    var email by remember { mutableStateOf(storedEmail) }
    var password by remember { mutableStateOf("") }  // Show blank instead of hashed on UI
    var contact by remember { mutableStateOf(storedContact) }
    var imageUri by remember { mutableStateOf<Uri?>(if (storedImageUriString.isNotEmpty()) Uri.parse(storedImageUriString) else null) }

    var isEditable by remember { mutableStateOf(false) }
    var showImageSourceDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedPath = copyImageToInternalStorage(context, it)
            if (savedPath != null) {
                imageUri = Uri.fromFile(File(savedPath))
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val savedPath = saveBitmapToInternalStorage(context, it)
            if (savedPath != null) {
                imageUri = Uri.fromFile(File(savedPath))
            }
        }
    }

    LaunchedEffect(storedUsername, storedEmail, storedPassword, storedContact, storedImageUriString) {
        username = storedUsername
        email = storedEmail
        // Don't show hashed password in UI for security; keep empty or masked
        password = ""
        contact = storedContact
        imageUri = if (storedImageUriString.isNotEmpty()) Uri.parse(storedImageUriString) else null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .border(3.dp, Color.White, CircleShape)
                .clickable(enabled = isEditable) {
                    showImageSourceDialog = true
                },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(text = "Tap to upload", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Internet Connection: ${connectionStatus.name}",
            style = MaterialTheme.typography.bodyMedium,
            color = when (connectionStatus) {
                ConnectionStatus.Available -> Color.Green
                ConnectionStatus.Losing -> Color.Yellow
                ConnectionStatus.Lost, ConnectionStatus.Unavailable -> Color.Red
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        ProfileTextField("Username", username, isEditable) { username = it }
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField("Email", email, isEditable) { email = it }
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField("Password", password, isEditable) { password = it }
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField("Contact", contact, isEditable) { contact = it }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                if (isEditable) {
                    coroutineScope.launch {
                        val hashedPassword = hashPassword(password)
                        profileDataStore.saveProfile(
                            username,
                            email,
                            hashedPassword,
                            contact,
                            imageUri
                        )
                        showStyledToast(context, "Profile saved successfully!")
                    }
                }
                isEditable = !isEditable
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = if (isEditable) "Save" else "Edit", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(24.dp)) // Space between buttons

        Button(
            onClick = {
                authViewModel1.signout()
                showStyledToast(context, "Logged out successfully!")
                navController.navigate("login") {
                    popUpTo("profile") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Logout", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(90.dp))
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Change Profile Image") },
            text = { Text("Choose image source:") },
            confirmButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Gallery")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    cameraLauncher.launch(null)
                }) {
                    Text("Camera")
                }
            }
        )
    }
}

@Composable
fun ProfileTextField(label: String, value: String, isEditable: Boolean, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
        enabled = isEditable,
        singleLine = true,
        visualTransformation = if (label == "Password") PasswordVisualTransformation() else VisualTransformation.None
    )
}

// Hash the password using SHA-256
fun hashPassword(password: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val bytes = md.digest(password.toByteArray(Charsets.UTF_8))
    return bytes.joinToString("") { "%02x".format(it) }
}

// Custom styled Toast function
fun showStyledToast(context: Context, message: String) {
    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
    val toastView = toast.view
    toastView?.setBackgroundColor(AndroidColor.parseColor("#6200EE"))  // Purple 700 from Material palette
    val text = toastView?.findViewById<TextView>(android.R.id.message)
    text?.setTextColor(AndroidColor.WHITE)
    text?.textSize = 16f
    toast.show()
}

// Helper function to copy image into internal storage
fun copyImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val fileName = "profile_image.jpg"
        val file = File(context.filesDir, fileName)
        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Helper to save camera bitmap to internal storage
fun saveBitmapToInternalStorage(context: Context, bitmap: android.graphics.Bitmap): String? {
    return try {
        val fileName = "profile_image_camera.jpg"
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, out)
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
