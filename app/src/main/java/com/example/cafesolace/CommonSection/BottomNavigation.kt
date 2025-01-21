package com.example.cafesolace.CommonSection

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
import com.example.cafesolace.Model.NavIterm
import com.example.cafesolace.Pages.MainScreen
import com.example.cafesolace.Pages.ProductScreen
import com.example.cafesolace.Pages.ProfilePage
import com.example.cafesolace.Pages.WishlistPage

@Composable
fun BottomNavigationScreen(modifier: Modifier = Modifier) {

    val navItermList = listOf(
        NavIterm("Home" , Icons.Default.Home),
        NavIterm("Product" , Icons.Default.Search),
        NavIterm("WishList" , Icons.Default.List),
        NavIterm("Profile" , Icons.Default.Person),

    )
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold (modifier = Modifier.fillMaxSize(),
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
        ){ innerPadding ->
        contentScreen(modifier = Modifier.padding(innerPadding),selectedIndex)
    }
}
@Composable
fun contentScreen (modifier: Modifier = Modifier,selectedIndex :Int ) {
when(selectedIndex){
    0->MainScreen()
    1->ProductScreen()
    2->WishlistPage()
    3-> ProfilePage()
}
}