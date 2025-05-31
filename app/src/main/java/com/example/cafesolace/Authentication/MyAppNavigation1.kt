package com.example.cafesolace.Authentication

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cafesolace.CommonSection.BottomNavigationScreen
import com.example.cafesolace.Pages.DessertScreen
import com.example.cafesolace.Pages.FeaturedDrinksPage
import com.example.cafesolace.Pages.FoodScreen
import com.example.cafesolace.Pages.MainScreen
import com.example.cafesolace.Pages.Master2Screen

@Composable
fun MyAppNavigation1(
    modifier: Modifier = Modifier,
    authViewModel1: AuthViewModel1,
    navController: NavHostController // Navigation controller to handle navigation between screens
) {
    // NavHost defines the navigation graph of the app
    NavHost(
        navController = navController,
        startDestination = "login", // Set the starting screen of the app
        modifier = modifier
    ) {
        // Composable destination for the login screen
        composable("login") {
            LoginPage1(modifier, navController, authViewModel1)
        }
        // Composable destination for the signup screen
        composable("signup") {
            SignUpPage1(modifier, navController, authViewModel1)
        }
        // Composable destination for the home screen with bottom navigation
        composable("home") {
            BottomNavigationScreen(modifier, navController, authViewModel1)
        }
        // Composable destination for MainScreen page
        composable("MainScreen") {
            MainScreen(navController)
        }
        // Composable destination for Master2Screen page
        composable("master2") {
            Master2Screen(navController)
        }
        // Composable destination for DessertScreen page
        composable("DessertScreen") {
            DessertScreen(navController)
        }
        composable("FoodScreen") {
            FoodScreen(navController)
        }
        composable("FeaturedDrinksPage") {
            FeaturedDrinksPage(navController)
        }
    }
}
