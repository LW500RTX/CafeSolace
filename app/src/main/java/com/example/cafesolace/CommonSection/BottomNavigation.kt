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
import com.example.cafesolace.Pages.ProfilePage
import com.example.cafesolace.Pages.WishlistPage
import com.example.cafesolace.ui.theme.AuthViewModel

@Composable
fun BottomNavigationScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel1: AuthViewModel1) {

    val navItermList = listOf(
        NavIterm("Home", Icons.Default.Home),
        NavIterm("Product", Icons.Default.Search),
        NavIterm("WishList", Icons.Default.List),
        NavIterm("Profile", Icons.Default.Person),
    )
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItermList.forEachIndexed { index, navIterm ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
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
        AnimatedContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex = selectedIndex,
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
        targetState = selectedIndex,
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { if (targetState > initialState) it else -it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)) with
                    slideOutHorizontally(
                        targetOffsetX = { if (targetState > initialState) -it else it },
                        animationSpec = tween(300)
                    ) + fadeOut(animationSpec = tween(300))
        }
    ) { targetIndex ->
        when (targetIndex) {
            0 -> MainScreen(navController)
            1 -> ProductScreen(navController = navController)
            2 -> WishlistPage()
            3 -> ProfilePage(authViewModel1= authViewModel1,navController=navController)


        }
    }
}
