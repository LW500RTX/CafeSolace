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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

    // DataStore instance for reading and writing profile data persistently
    val profileDataStore = remember { ProfileDataStore(context) }

    // Connectivity observer to track internet connection status
    val connectivityObserver = remember { ConnectivityObserver(context) }
    val connectionStatus by connectivityObserver.connectionStatus.collectAsState()

    // Collect stored user profile data from DataStore as State
    val storedUsername by profileDataStore.getUsername.collectAsState(initial = "")
    val storedEmail by profileDataStore.getEmail.collectAsState(initial = "")
    val storedPassword by profileDataStore.getPassword.collectAsState(initial = "")
    val storedContact by profileDataStore.getContact.collectAsState(initial = "")
    val storedImageUriString by profileDataStore.getImageUri.collectAsState(initial = "")

    // Mutable state for UI fields, initialized with stored values
    var username by remember { mutableStateOf(storedUsername) }
    var email by remember { mutableStateOf(storedEmail) }
    var password by remember { mutableStateOf("") } // Do not prefill password for security
    var contact by remember { mutableStateOf(storedContact) }
    var imageUri by remember { mutableStateOf<Uri?>(if (storedImageUriString.isNotEmpty()) Uri.parse(storedImageUriString) else null) }

    // Controls whether the fields are editable
    var isEditable by remember { mutableStateOf(false) }

    // Controls visibility of the dialog to select image source (camera or gallery)
    var showImageSourceDialog by remember { mutableStateOf(false) }

    // Launcher to pick image from gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Copy the selected image to internal storage and update imageUri
            val savedPath = copyImageToInternalStorage(context, it)
            if (savedPath != null) {
                imageUri = Uri.fromFile(File(savedPath))
            }
        }
    }

    // Launcher to take a picture using the camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            // Save the captured bitmap to internal storage and update imageUri
            val savedPath = saveBitmapToInternalStorage(context, it)
            if (savedPath != null) {
                imageUri = Uri.fromFile(File(savedPath))
            }
        }
    }

    // Update UI state when stored profile data changes
    LaunchedEffect(storedUsername, storedEmail, storedPassword, storedContact, storedImageUriString) {
        username = storedUsername
        email = storedEmail
        password = "" // Clear password input field for security
        contact = storedContact
        imageUri = if (storedImageUriString.isNotEmpty()) Uri.parse(storedImageUriString) else null
    }

    val colors = MaterialTheme.colorScheme
    val scrollState = rememberScrollState()

    // Main UI Column with vertical scroll and padding
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(colors.background)
            .padding(horizontal = 20.dp)
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile image container with circular shape, border, and click to change image (if editable)
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(colors.onSurface.copy(alpha = 0.2f))
                .border(3.dp, colors.primary, CircleShape)
                .clickable(enabled = isEditable) {
                    showImageSourceDialog = true
                },
            contentAlignment = Alignment.Center
        ) {
            // Display profile image or placeholder text
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(text = "Tap to upload", color = colors.onSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display internet connection status with color coding
        Text(
            text = "Internet Connection: ${connectionStatus.name}",
            style = MaterialTheme.typography.bodyMedium,
            color = when (connectionStatus) {
                ConnectionStatus.Available -> Color(0xFF4CAF50) // Green
                ConnectionStatus.Losing -> Color(0xFFFFC107)   // Amber
                ConnectionStatus.Lost, ConnectionStatus.Unavailable -> Color(0xFFF44336) // Red
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Editable text fields for profile info, enabled only when isEditable == true
        ProfileTextField("Username", username, isEditable) { username = it }
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField("Email", email, isEditable) { email = it }
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField("Password", password, isEditable) { password = it }
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField("Contact", contact, isEditable) { contact = it }

        Spacer(modifier = Modifier.height(40.dp))

        // Button toggles between Edit and Save states
        Button(
            onClick = {
                if (isEditable) {
                    // Save profile data to DataStore asynchronously
                    coroutineScope.launch {
                        val hashedPassword = hashPassword(password) // Hash password before saving
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
                isEditable = !isEditable // Toggle editing state
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = if (isEditable) "Save" else "Edit", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logout button signs out and navigates to login screen
        Button(
            onClick = {
                authViewModel1.signout()
                showStyledToast(context, "Logged out successfully!")
                navController.navigate("login") {
                    popUpTo("profile") { inclusive = true }  // Clear profile screen from back stack
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.error,
                contentColor = colors.onError
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Logout", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(90.dp))
    }

    // Dialog for choosing image source (Gallery or Camera)
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Change Profile Image") },
            text = { Text("Choose image source:") },
            confirmButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    galleryLauncher.launch("image/*")  // Launch gallery picker
                }) {
                    Text("Gallery")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    cameraLauncher.launch(null)  // Launch camera preview
                }) {
                    Text("Camera")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTextField(label: String, value: String, isEditable: Boolean, onValueChange: (String) -> Unit) {
    val colors = MaterialTheme.colorScheme
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .border(1.dp, colors.outline, RoundedCornerShape(8.dp)),
        enabled = isEditable,  // Enable only if editable
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = colors.onSurface.copy(alpha = 0.6f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            containerColor = colors.surface,
            cursorColor = colors.primary
        ),
        visualTransformation = if (label == "Password") PasswordVisualTransformation() else VisualTransformation.None
    )
}

// Hash the password securely using SHA-256 before saving
fun hashPassword(password: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val bytes = md.digest(password.toByteArray(Charsets.UTF_8))
    return bytes.joinToString("") { "%02x".format(it) }
}

// Show a custom styled Toast message with purple background and white text
fun showStyledToast(context: Context, message: String) {
    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
    val toastView = toast.view
    toastView?.setBackgroundColor(AndroidColor.parseColor("#6200EE"))  // Purple 700
    val text = toastView?.findViewById<TextView>(android.R.id.message)
    text?.setTextColor(AndroidColor.WHITE)
    text?.textSize = 16f
    toast.show()
}

// Copy image picked from gallery into app's internal storage for persistent access
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

// Save a bitmap taken by camera into internal storage and return file path
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
