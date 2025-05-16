package com.example.cafesolace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cafesolace.Authentication.AuthViewModel1
import com.example.cafesolace.Authentication.MyAppNavigation1
import com.example.cafesolace.ui.theme.CafeSolaceTheme
import kotlinx.coroutines.delay
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Ensure full edge-to-edge content
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val isDarkTheme = remember { mutableStateOf(isDarkModeTime()) }

            // Update theme every minute
            LaunchedEffect(Unit) {
                while (true) {
                    isDarkTheme.value = isDarkModeTime()
                    delay(60000)
                }
            }

            CafeSolaceTheme(darkTheme = isDarkTheme.value) {
                val navController = rememberNavController()
                val authViewModel1: AuthViewModel1 by viewModels()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(), // Push content below status bar without gap
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
fun WeatherSuggestionDialog(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var suggestion by remember { mutableStateOf("") }
    var drinkEmoji by remember { mutableStateOf("") }

    val temperature = 31.0 // Replace with real data later

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Crossfade(targetState = drinkEmoji) { emoji ->
                        Text(
                            text = emoji,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    Text("Drink Suggestion")
                }
            },
            text = { Text(suggestion) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        navController.navigate("master2")
                    }
                ) {
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
