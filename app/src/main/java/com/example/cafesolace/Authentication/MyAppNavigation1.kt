package com.example.cafesolace.Authentication


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cafesolace.CommonSection.BottomNavigationScreen
import com.example.cafesolace.Pages.MainScreen
import com.example.cafesolace.Pages.Master2Screen

@Composable
fun MyAppNavigation1(modifier: Modifier = Modifier,authViewModel1: AuthViewModel1) {
val navController = rememberNavController()
    NavHost(navController = navController , startDestination = "login", builder = {
        composable("login"){
            LoginPage1(modifier,navController,authViewModel1)
        }
        composable("signup"){
            SignUpPage1(modifier,navController,authViewModel1)
        }
        composable("home"){
            BottomNavigationScreen(modifier,navController,authViewModel1)
        }
        composable("main") {  // Ensure this route exists
            MainScreen(navController) // Pass navController here
        }
        composable("master2") {

            Master2Screen(navController)
        }
    })
}