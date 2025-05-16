package com.example.cafesolace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

        setContent {
            val isDarkTheme = remember { mutableStateOf(isDarkModeTime()) }

            // Periodically check time every minute
            LaunchedEffect(Unit) {
                while (true) {
                    isDarkTheme.value = isDarkModeTime()
                    delay(60000) // 1 minute
                }
            }

            CafeSolaceTheme(darkTheme = isDarkTheme.value) {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val authViewModel1: AuthViewModel1 by viewModels()
                    MyAppNavigation1(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel1 = authViewModel1
                    )
                }
            }
        }
    }

    private fun isDarkModeTime(): Boolean {
        val now = LocalTime.now()
        val start = LocalTime.of(17, 0) // 5:00 PM
        val end = LocalTime.of(5, 0)   // 5:00 AM

        return if (start < end) {
            now >= start && now < end
        } else {
            now >= start || now < end
        }
    }
}
