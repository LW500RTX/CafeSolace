package com.example.cafesolace.Pages

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.cafesolace.R
import kotlinx.coroutines.delay

data class Dessert(val name: String, val price: String, val imageRes: Int)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DessertScreen(navController: NavController) {
    val desserts = listOf(
        Dessert("Espresso", "Rs.700.00", R.drawable.capuchino),
        Dessert("Latte", "Rs.790.00", R.drawable.latte01),
        Dessert("Cappuccino", "Rs.450.00", R.drawable.caffeine),
        Dessert("Mocha", "Rs.500.00", R.drawable.mocha),
        Dessert("Latte new", "Rs.800.00", R.drawable.latte25),
        Dessert("Coffee", "Rs.200.00", R.drawable.a),
        Dessert("Mango Juice", "Rs.300.00", R.drawable.b),
        Dessert("Faluda", "Rs.570.00", R.drawable.c),
    )

    var searchQuery by remember { mutableStateOf("") }
    val filteredDesserts = desserts.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    var searchBarVisible by remember { mutableStateOf(false) }
    var cardsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        searchBarVisible = true
        delay(300)
        cardsVisible = true
    }

    Scaffold(
        topBar = { DessertTopBar(navController) },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                AnimatedVisibility(
                    visible = searchBarVisible,
                    enter = slideInVertically(
                        initialOffsetY = { -100 },
                        animationSpec = tween(durationMillis = 300)
                    ) + fadeIn(animationSpec = tween(300))
                ) {
                    SearchBar(query = searchQuery, onQueryChanged = { searchQuery = it })
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredDesserts) { dessert ->
                        AnimatedVisibility(
                            visible = cardsVisible,
                            enter = fadeIn(animationSpec = tween(300)) +
                                    scaleIn(initialScale = 0.9f, animationSpec = tween(300))
                        ) {
                            DessertCard(dessert = dessert)
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DessertTopBar(navController: NavController) {
    TopAppBar(
        title = { Text("TRENDING") },
        navigationIcon = {
            IconButton(onClick = { navController.navigate("MainScreen") }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = { /* handle cart */ }) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text("Search") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search Icon")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFDDE4FF)),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFDDE4FF))
    )
}

@Composable
fun DessertCard(dessert: Dessert) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = dessert.imageRes),
                contentDescription = dessert.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Text(dessert.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(dessert.price, fontSize = 14.sp)
            Button(
                onClick = { /* handle buy */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Buy", color = Color.White)
            }
        }
    }
}
