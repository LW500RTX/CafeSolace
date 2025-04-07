package com.example.cafesolace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cafesolace.Authentication.AuthViewModel1
import com.example.cafesolace.Authentication.MyAppNavigation1
import com.example.cafesolace.CommonSection.BottomNavigationScreen
import com.example.cafesolace.CommonSection.MyAppNavigation
import com.example.cafesolace.Pages.LoginPage
import com.example.cafesolace.Pages.MainScreen
import com.example.cafesolace.Pages.ProductScreen
import com.example.cafesolace.Pages.ProfilePage
import com.example.cafesolace.Pages.WishlistPage
import com.example.cafesolace.ui.theme.AuthViewModel
import com.example.cafesolace.ui.theme.CafeSolaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val authViewModel: AuthViewModel by viewModels()
        setContent {
            CafeSolaceTheme {
                val navController = rememberNavController()
//               MainScreen()
//                ProductScreen()
//                ProfilePage()
//                WishlistPage()na
//                BottomNavigationScreen(modifier = )
//                LoginPage()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MyAppNavigation(
//                        modifier = Modifier.padding(innerPadding), authViewModel = authViewModel
//                    )
                    val authViewModel1 :AuthViewModel1 by viewModels()
                    MyAppNavigation1(modifier = Modifier.padding(innerPadding), authViewModel1 = authViewModel1)

//                    )
//                }
                }

            }
        }
    }
}



