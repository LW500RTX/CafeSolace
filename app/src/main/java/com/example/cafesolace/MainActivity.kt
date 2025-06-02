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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.flow.first
import java.time.LocalTime
import androidx.compose.foundation.border
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.layout.ContentScale
import kotlin.math.roundToInt


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge drawing for immersive UI experience
        enableEdgeToEdge()

        // Disable default system window fitting to allow Compose to handle insets
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Request WRITE_SETTINGS permission to adjust system brightness dynamically
        if (!Settings.System.canWrite(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
        }

        // Set Jetpack Compose content
        setContent {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()

            // State for current theme mode: null means not set, fallback to time-based theme
            var isDarkTheme by remember { mutableStateOf<Boolean?>(null) }

            // Initialize theme preference from saved preferences or time-based fallback
            LaunchedEffect(Unit) {
                val savedTheme = ThemePreference.getTheme(context).first()
                isDarkTheme = savedTheme ?: isDarkModeTime()

                // If no saved preference, update theme automatically every minute based on time
                if (savedTheme == null) {
                    while (true) {
                        isDarkTheme = isDarkModeTime()
                        delay(60000)  // Check every 60 seconds
                    }
                }
            }

            // Apply custom theme with darkTheme parameter
            CafeSolaceTheme(darkTheme = isDarkTheme ?: false) {
                val navController = rememberNavController()
                val authViewModel1: AuthViewModel1 by viewModels()

                // Control screen brightness based on ambient light sensor
                AmbientLightBrightnessController(context = this@MainActivity)

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),  // Add padding for system bars (status/navigation)
                    topBar = {
                        // Top app bar with stylized title and theme toggle buttons
                        TopAppBar(
                            title = {
                                Text(
                                    text = buildAnnotatedString {
                                        // "Cafe " part with default color and bold font
                                        withStyle(
                                            style = SpanStyle(
                                                color = MaterialTheme.colorScheme.onBackground,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 34.sp
                                            )
                                        ) {
                                            append("Cafe ")
                                        }
                                        // "Solace" part in red error color with bold font
                                        withStyle(
                                            style = SpanStyle(
                                                color = MaterialTheme.colorScheme.error,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 34.sp
                                            )
                                        ) {
                                            append("Solace")
                                        }
                                    }
                                )
                            },
                            actions = {
                                // Theme toggle button (dark/light mode)
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        val newTheme = !(isDarkTheme ?: false)
                                        ThemePreference.setTheme(context, newTheme)
                                        isDarkTheme = newTheme
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (isDarkTheme == true) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = "Toggle Theme",
                                        tint = if (isDarkTheme == true) Color.Yellow else Color.Black
                                    )


                                }
                                // Reset theme to automatic (time-based)
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        ThemePreference.clearTheme(context)
                                        isDarkTheme = isDarkModeTime()
                                    }
                                }) {
                                    Icon(Icons.Default.Restore, contentDescription = "Auto Theme")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                            .fillMaxSize()
                    ) {
                        // Main app navigation content (authentication and screens)
                        MyAppNavigation1(
                            modifier = Modifier.fillMaxSize(),
                            authViewModel1 = authViewModel1,
                            navController = navController
                        )
                        // Show weather-based drink suggestion dialog
                        WeatherSuggestionDialog(navController = navController)
                        // Display draggable chatbot widget
                        ChatBotWidget()
                    }
                }
            }
        }
    }

    // Helper function to determine if current time should be dark mode (5 PM to 5 AM)
    private fun isDarkModeTime(): Boolean {
        val now = LocalTime.now()
        val start = LocalTime.of(17, 0)
        val end = LocalTime.of(5, 0)
        // Handles overnight period crossing midnight
        return if (start < end) now >= start && now < end else now >= start || now < end
    }
}

// ----- Sensors -----
// Composable that observes ambient light sensor and adjusts screen brightness accordingly
@Composable
fun AmbientLightBrightnessController(context: Context) {
    // Collect ambient light sensor lux value as state
    val lux by AmbientLightSensorValue(context).collectAsState(initial = 0f)

    // Map lux (0 - 10000) to brightness (30 - 255)
    val brightness = (lux / 10000f * 255).toInt().coerceIn(30, 255)

    // Update system screen brightness when brightness value changes
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

// Provides a StateFlow emitting ambient light sensor lux values
@Composable
fun AmbientLightSensorValue(context: Context): StateFlow<Float> {
    val luxFlow = MutableStateFlow(0f)
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                luxFlow.value = event.values[0] // Update lux value on sensor change
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensorManager.registerListener(listener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose { sensorManager.unregisterListener(listener) }
    }

    return luxFlow.asStateFlow()
}

// Provides a StateFlow emitting ambient temperature sensor values
@Composable
fun AmbientTemperatureSensorValue(context: Context): StateFlow<Float> {
    val tempFlow = MutableStateFlow(0f)
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                tempFlow.value = event.values[0]  // Update temperature on sensor change
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        // Register listener only if temperature sensor is available
        if (tempSensor != null) {
            sensorManager.registerListener(listener, tempSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        onDispose {
            if (tempSensor != null) {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    return tempFlow.asStateFlow()
}

// ----- Weather Suggestion -----
// Dialog that shows a drink suggestion based on current ambient temperature
@Composable
fun WeatherSuggestionDialog(navController: NavHostController) {
    val context = LocalContext.current
    val temperature by AmbientTemperatureSensorValue(context).collectAsState(initial = 25f)

    // Controls whether the suggestion dialog is shown
    var showDialog by remember { mutableStateOf(false) }

    // Drink suggestion text and emoji icon
    var suggestion by remember { mutableStateOf("") }
    var drinkEmoji by remember { mutableStateOf("") }

    // When temperature changes, wait 3 seconds then show appropriate suggestion
    LaunchedEffect(temperature) {
        delay(3000L)  // Small delay to avoid abrupt popup
        suggestion = when {
            temperature > 28 -> {
                drinkEmoji = "\u2744️"  // Snowflake emoji for hot (cold drink)
                "It's hot today! Would you like a cold drink?"
            }
            temperature < 20 -> {
                drinkEmoji = "\u2615"  // Hot beverage emoji for cold weather
                "It's chilly today! How about a hot drink?"
            }
            else -> {
                drinkEmoji = "\uD83C\uDF7A"  // Beer mug emoji for pleasant weather
                "The weather is pleasant. Pick what you like!"
            }
        }
        showDialog = true
    }

    // Display alert dialog if showDialog is true
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Animate emoji change
                    Crossfade(targetState = drinkEmoji) { emoji ->
                        Text(emoji, style = MaterialTheme.typography.headlineMedium)
                    }
                    Text("Drink Suggestion")
                }
            },
            text = { Text(suggestion) },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.navigate("master2")  // Navigate to drink selection screen
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotWidget() {
    var isExpanded by remember { mutableStateOf(false) }
    val chatMessages = remember { mutableStateListOf("Cafe Solace AI: Hi there! How can I help you today?") }
    var currentInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var offset by remember { mutableStateOf(Offset(8f, 0f)) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        if (isExpanded) {
            Surface(
                modifier = Modifier
                    .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offset += dragAmount
                        }
                    }
                    .width(320.dp)
                    .height(480.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp,
                color = Color(0xFF000000) // Dark brown background
            ) {
                Column {
                    // Header with gradient and profile + close button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFEAA2D2), Color(0xFFFBC8C4))
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.man),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Solace AI", fontWeight = FontWeight.Bold, color = Color.Black)
                                    Text("Customer Support Specialist", fontSize = 12.sp, color = Color.DarkGray)
                                }
                            }
                            IconButton(onClick = { isExpanded = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
                            }
                        }
                    }

                    // Chat messages
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        for (msg in chatMessages) {
                            Text(msg, modifier = Modifier.padding(vertical = 4.dp), color = Color.White)
                        }
                    }

                    // Input and send
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = currentInput,
                            onValueChange = { currentInput = it },
                            placeholder = { Text("Type here...", color = Color.Gray) },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {
                            if (currentInput.isNotBlank()) {
                                chatMessages.add("You: $currentInput")
                                val inputCopy = currentInput
                                currentInput = ""
                                scope.launch(Dispatchers.IO) {
                                    val botResponse = fakeChatBotResponse(inputCopy)
                                    launch(Dispatchers.Main) {
                                        chatMessages.add("Bot: $botResponse")
                                    }
                                }
                            }
                        }) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                        }
                    }

                    // Footer buttons and branding
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconWithLabel(Icons.Default.ChatBubble, "Chat")
                            IconWithLabel(Icons.Default.GraphicEq, "Voice")
                            IconWithLabel(Icons.Default.History, "History")
                        }
                        Text(
                            text = "Powered by Open Ai",
                            fontSize = 10.sp,
                            color = Color.LightGray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        } else {
            FloatingActionButton(
                onClick = { isExpanded = true },
                modifier = Modifier
                    .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offset += dragAmount
                        }
                    }
                    .padding(16.dp),
                shape = CircleShape,
                containerColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.man), // Replace with your file name
                    contentDescription = "Chat Bot",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }

        }
    }
}


@Composable
fun IconWithLabel(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = Color.White)
        Text(label, fontSize = 12.sp, color = Color.White)
    }
}
suspend fun fakeChatBotResponse(input: String): String {
    delay(1000)
    return when {
        "coffee" in input.lowercase() -> "How about trying our signature espresso today?"
        "tea" in input.lowercase() -> "We have a refreshing mint tea. Would you like to try it?"
        "cold" in input.lowercase() -> "Our iced latte is a perfect choice!"
        else -> "That's interesting! Can you tell me more?"
    }
}