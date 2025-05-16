package com.example.cafesolace.Authentication

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cafesolace.CommonSection.BottomNavigationScreen
import com.example.cafesolace.Pages.MainScreen
import com.example.cafesolace.Pages.Master2Screen

@Composable
fun MyAppNavigation1(
    modifier: Modifier = Modifier,
    authViewModel1: AuthViewModel1,
    navController: NavHostController // Use NavHostController here
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginPage1(modifier, navController, authViewModel1)
        }
        composable("signup") {
            SignUpPage1(modifier, navController, authViewModel1)
        }
        composable("home") {
            BottomNavigationScreen(modifier, navController, authViewModel1)
        }
        composable("main") {
            MainScreen(navController)
        }
        composable("master2") {
            Master2Screen(navController)
        }
    }
}
