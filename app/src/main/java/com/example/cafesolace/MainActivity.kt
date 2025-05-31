package com.example.cafesolace

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cafesolace.Authentication.AuthViewModel1
import com.example.cafesolace.Authentication.MyAppNavigation1
import com.example.cafesolace.ui.theme.CafeSolaceTheme
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalTime

// MainActivity - Entry point of the app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge content (draw behind system bars)
        enableEdgeToEdge()
        // Disable default system window fitting to use edge-to-edge properly
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Request permission to modify system settings (e.g., screen brightness)
        if (!Settings.System.canWrite(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
        }

        // Set the Compose UI content
        setContent {
            // Mutable state for whether dark theme is enabled based on time
            val isDarkTheme = remember { mutableStateOf(isDarkModeTime()) }

            // Update the theme every minute by checking the time
            LaunchedEffect(Unit) {
                while (true) {
                    isDarkTheme.value = isDarkModeTime()
                    delay(60000) // Delay 1 minute
                }
            }

            // Apply the app theme (dark or light)
            CafeSolaceTheme(darkTheme = isDarkTheme.value) {
                val navController = rememberNavController() // Navigation controller for Compose Navigation
                val authViewModel1: AuthViewModel1 by viewModels() // ViewModel for Authentication

                // Control screen brightness based on ambient light sensor
                AmbientLightBrightnessController(context = this@MainActivity)

                // Scaffold provides basic layout structure with padding for system bars
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) { innerPadding ->
                    // Box container for navigation, dialogs, and chatbot widget
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                            .fillMaxSize()
                    ) {
                        // Main app navigation composable, handling app screens
                        MyAppNavigation1(
                            modifier = Modifier.fillMaxSize(),
                            authViewModel1 = authViewModel1,
                            navController = navController
                        )
                        // Show weather-based drink suggestion dialog
                        WeatherSuggestionDialog(navController = navController)
                        // Floating chatbot widget
                        ChatBotWidget()
                    }
                }
            }
        }
    }

    // Helper function to decide if dark mode should be enabled based on time
    private fun isDarkModeTime(): Boolean {
        val now = LocalTime.now()
        val start = LocalTime.of(17, 0) // 5 PM
        val end = LocalTime.of(5, 0)    // 5 AM
        // Return true if current time is between 5 PM and 5 AM (overnight)
        return if (start < end) {
            now >= start && now < end
        } else {
            now >= start || now < end
        }
    }
}

// Composable controlling screen brightness according to ambient light sensor
@Composable
fun AmbientLightBrightnessController(context: Context) {
    // Collect ambient light sensor values as a StateFlow
    val lux by AmbientLightSensorValue(context).collectAsState(initial = 0f)
    // Convert lux to a brightness value between 30 and 255
    val brightness = (lux / 10000f * 255).toInt().coerceIn(30, 255)

    // Side effect to update system screen brightness whenever brightness changes
    LaunchedEffect(brightness) {
        if (Settings.System.canWrite(context)) {
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                brightness
            )
        }
    }
}

// Composable that returns a StateFlow of ambient light sensor values (lux)
@Composable
fun AmbientLightSensorValue(context: Context): StateFlow<Float> {
    val luxFlow = MutableStateFlow(0f) // Mutable state for light sensor readings
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    DisposableEffect(Unit) {
        // SensorEventListener to update luxFlow on sensor changes
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                luxFlow.value = event.values[0]
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        // Register the listener with normal delay
        sensorManager.registerListener(listener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)

        // Unregister listener when composable leaves composition
        onDispose { sensorManager.unregisterListener(listener) }
    }

    return luxFlow.asStateFlow() // Return immutable StateFlow
}

// Composable that returns a StateFlow of ambient temperature sensor values (°C)
@Composable
fun AmbientTemperatureSensorValue(context: Context): StateFlow<Float> {
    val tempFlow = MutableStateFlow(0f) // Mutable state for temperature readings
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

    DisposableEffect(Unit) {
        // SensorEventListener to update tempFlow on sensor changes
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                tempFlow.value = event.values[0]
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        // Register listener only if temperature sensor exists
        if (tempSensor != null) {
            sensorManager.registerListener(listener, tempSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        // Unregister listener when composable leaves composition
        onDispose {
            if (tempSensor != null) {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    return tempFlow.asStateFlow() // Return immutable StateFlow
}

// Composable to display a weather-based drink suggestion dialog
@Composable
fun WeatherSuggestionDialog(navController: NavHostController) {
    val context = LocalContext.current
    // Collect ambient temperature value from sensor, default to 25°C
    val temperature by AmbientTemperatureSensorValue(context).collectAsState(initial = 25f)

    // State to control whether dialog is visible
    var showDialog by remember { mutableStateOf(false) }
    var suggestion by remember { mutableStateOf("") } // Suggestion message text
    var drinkEmoji by remember { mutableStateOf("") } // Emoji icon for suggestion

    // Show the dialog 3 seconds after temperature updates
    LaunchedEffect(temperature) {
        delay(3000L) // 3 seconds delay
        // Determine suggestion and emoji based on temperature ranges
        suggestion = when {
            temperature > 28 -> {
                drinkEmoji = "\u2744️" // Snowflake emoji
                "It's hot today! Would you like a cold drink?"
            }
            temperature < 20 -> {
                drinkEmoji = "\u2615" // Hot beverage emoji
                "It's chilly today! How about a hot drink?"
            }
            else -> {
                drinkEmoji = "\uD83C\uDF7A" // Beer mug emoji
                "The weather is pleasant. Pick what you like!"
            }
        }
        showDialog = true // Show the dialog
    }

    // Show AlertDialog when showDialog is true
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Animate emoji transition
                    Crossfade(targetState = drinkEmoji) { emoji ->
                        Text(emoji, style = MaterialTheme.typography.headlineMedium)
                    }
                    Text("Drink Suggestion")
                }
            },
            text = { Text(suggestion) }, // Suggestion message text
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.navigate("DessertScreen") // Navigate to drink selection screen
                }) {
                    Text("Choose a Drink")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Maybe Later")
                }
            }
        )
    }
}

// Composable implementing a draggable floating ChatBot widget
@Composable
fun ChatBotWidget() {
    var isExpanded by remember { mutableStateOf(false) } // Whether the chat window is expanded
    val chatMessages = remember {
        mutableStateListOf("Cafe Solace Ai: Hi there! How can I")
    }
}
