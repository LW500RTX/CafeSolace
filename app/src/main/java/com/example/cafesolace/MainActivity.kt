package com.example.cafesolace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cafesolace.Pages.MainScreen
import com.example.cafesolace.Pages.ProductScreen
import com.example.cafesolace.Pages.ProfilePage
import com.example.cafesolace.Pages.WishlistPage
import com.example.cafesolace.ui.theme.CafeSolaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CafeSolaceTheme {
               MainScreen()
//                ProductScreen()
//                ProfilePage()
//                WishlistPage()
            }
        }
    }
}

