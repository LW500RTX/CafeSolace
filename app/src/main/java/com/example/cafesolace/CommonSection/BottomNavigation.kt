package com.example.cafesolace.CommonSection

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cafesolace.Authentication.AuthViewModel1
import com.example.cafesolace.Model.NavIterm
import com.example.cafesolace.Pages.MainScreen
import com.example.cafesolace.Pages.ProductScreen
import com.example.cafesolace.Pages.WishlistPage
import com.example.cafesolace.Screens.ProfilePage

@Composable
fun BottomNavigationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel1: AuthViewModel1
) {
    // List of navigation items with label and icon
    val navItermList = listOf(
        NavIterm("Home", Icons.Default.Home),
        NavIterm("Product", Icons.Default.Search),
        NavIterm("WishList", Icons.Default.List),
        NavIterm("Profile", Icons.Default.Person),
    )

    // Remember the currently selected index (default to first tab)
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(), // Fill the whole screen
        bottomBar = {
            // Bottom navigation bar container
            NavigationBar {
                // Iterate over all nav items and create a NavigationBarItem for each
                navItermList.forEachIndexed { index, navIterm ->
                    NavigationBarItem(
                        selected = selectedIndex == index, // Highlight if selected
                        onClick = {
                            selectedIndex = index // Update selected index on click
                        },
                        icon = {
                            Icon(imageVector = navIterm.icon, contentDescription = "Icon")
                        },
                        label = {
                            Text(text = navIterm.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Main content area with animation on tab switch
        AnimatedContentScreen(
            modifier = Modifier.padding(innerPadding), // Padding for scaffold inner content
            selectedIndex = selectedIndex, // Current selected tab
            navController = navController,
            authViewModel1 = authViewModel1
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavController,
    authViewModel1: AuthViewModel1
) {
    AnimatedContent(
        modifier = modifier,
        targetState = selectedIndex, // Animate based on selected tab index
        transitionSpec = {
            // Define enter and exit animations depending on tab navigation direction
            slideInHorizontally(
                initialOffsetX = { if (targetState > initialState) it else -it }, // Slide in from right if moving forward, else from left
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)) with // Fade in together with slide
                    slideOutHorizontally(
                        targetOffsetX = { if (targetState > initialState) -it else it }, // Slide out left if moving forward, else right
                        animationSpec = tween(300)
                    ) + fadeOut(animationSpec = tween(300)) // Fade out with slide out
        }
    ) { targetIndex ->
        // Show the corresponding screen for the selected tab
        when (targetIndex) {
            0 -> MainScreen(navController) // Home screen
            1 -> ProductScreen(navController = navController) // Product listing/search screen
            2 -> WishlistPage() // User wishlist screen
            3 -> ProfilePage(authViewModel1 = authViewModel1, navController = navController) // User profile screen
        }
    }
}
