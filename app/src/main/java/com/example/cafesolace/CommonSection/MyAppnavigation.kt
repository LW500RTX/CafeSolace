package com.example.cafesolace.CommonSection

import Master2Screen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cafesolace.Data.FoodItems
import com.example.cafesolace.Model.Main
import com.example.cafesolace.Pages.LoginPage
import com.example.cafesolace.Pages.MainScreen
import com.example.cafesolace.Pages.SignupPage
import com.example.cafesolace.Pages.detailviewPage
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

        composable("Bottomnavigation") {
            BottomNavigationScreen(modifier,navController,authViewModel)
        }
        composable("main") {  // Ensure this route exists
            MainScreen(navController) // Pass navController here
        }
        composable("master2") {
            Master2Screen(navController)
        }

        composable(
            route = "detailedProductView/{imageResId}",
            arguments = listOf(navArgument("imageResId") { type = NavType.IntType })
        ) { backStackEntry ->
            val imageResId = backStackEntry.arguments?.getInt("imageResId")
            val product = FoodItems().loadFoodItems().find { it.imageResId == imageResId }
            product?.let {
                MasterView(main = it, navController = navController)
            }
        }
//        composable("login") {
//            MainScreen(modifier,navController,authViewModel)
//        }
    })
}