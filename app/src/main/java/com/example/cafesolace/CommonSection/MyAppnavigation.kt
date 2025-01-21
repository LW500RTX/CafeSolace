package com.example.cafesolace.CommonSection

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cafesolace.Pages.LoginPage
import com.example.cafesolace.Pages.MainScreen
import com.example.cafesolace.Pages.SignupPage
import com.example.cafesolace.ui.theme.AuthViewModel

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier,authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login", builder = {
composable("login") {
    LoginPage(modifier,navController,authViewModel)
}
        composable("signup") {
            SignupPage(modifier,navController,authViewModel)
        }
//        composable("login") {
//            MainScreen(modifier,navController,authViewModel)
//        }
    })
}