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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (!Settings.System.canWrite(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
        }

        setContent {
            val isDarkTheme = remember { mutableStateOf(isDarkModeTime()) }

            LaunchedEffect(Unit) {
                while (true) {
                    isDarkTheme.value = isDarkModeTime()
                    delay(60000)
                }
            }

            CafeSolaceTheme(darkTheme = isDarkTheme.value) {
                val navController = rememberNavController()
                val authViewModel1: AuthViewModel1 by viewModels()

                AmbientLightBrightnessController(context = this@MainActivity)

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                            .fillMaxSize()
                    ) {
                        MyAppNavigation1(
                            modifier = Modifier.fillMaxSize(),
                            authViewModel1 = authViewModel1,
                            navController = navController
                        )
                        WeatherSuggestionDialog(navController = navController)
                        ChatBotWidget()
                    }
                }
            }
        }
    }

    private fun isDarkModeTime(): Boolean {
        val now = LocalTime.now()
        val start = LocalTime.of(17, 0)
        val end = LocalTime.of(5, 0)
        return if (start < end) {
            now >= start && now < end
        } else {
            now >= start || now < end
        }
    }
}

@Composable
fun AmbientLightBrightnessController(context: Context) {
    val lux by AmbientLightSensorValue(context).collectAsState(initial = 0f)
    val brightness = (lux / 10000f * 255).toInt().coerceIn(30, 255)

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

@Composable
fun AmbientLightSensorValue(context: Context): StateFlow<Float> {
    val luxFlow = MutableStateFlow(0f)
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                luxFlow.value = event.values[0]
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensorManager.registerListener(listener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose { sensorManager.unregisterListener(listener) }
    }

    return luxFlow.asStateFlow()
}

@Composable
fun WeatherSuggestionDialog(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var suggestion by remember { mutableStateOf("") }
    var drinkEmoji by remember { mutableStateOf("") }
    val temperature = 31.0

    LaunchedEffect(Unit) {
        delay(3000L)
        suggestion = when {
            temperature > 28 -> {
                drinkEmoji = "\u2744️"
                "It's hot today! Would you like a cold drink?"
            }

            temperature < 20 -> {
                drinkEmoji = "\u2615"
                "It's chilly today! How about a hot drink?"
            }

            else -> {
                drinkEmoji = "\uD83C\uDF7A"
                "The weather is pleasant. Pick what you like!"
            }
        }
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                    navController.navigate("master2")
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

@Composable
fun ChatBotWidget() {
    var isExpanded by remember { mutableStateOf(false) }
    val chatMessages = remember { mutableStateListOf("Cafe Solace Ai: Hi there! How can I help you today?") }
    var currentInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var offset by remember { mutableStateOf(Offset(8f, 00f)) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        if (isExpanded) {
            Surface(
                modifier = Modifier
                    .offset { offset.toIntOffset() }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offset += dragAmount
                        }
                    }
                    .width(300.dp)
                    .height(400.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 8.dp)
                    ) {
                        for (msg in chatMessages) {
                            Text(msg, modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        TextField(
                            value = currentInput,
                            onValueChange = { currentInput = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Ask me anything") },
                            maxLines = 3,
                            singleLine = false,
                        )
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
                            Icon(Icons.Default.Send, contentDescription = "Send")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(onClick = { isExpanded = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Close")
                    }
                }
            }
        } else {
            FloatingActionButton(
                onClick = { isExpanded = true },
                modifier = Modifier
                    .offset { offset.toIntOffset() }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offset += dragAmount
                        }
                    }
                    .padding(16.dp),
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Default.ChatBubble, contentDescription = "Chat Bot")
            }
        }
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

private fun Offset.toIntOffset() = IntOffset(x.toInt(), y.toInt())
