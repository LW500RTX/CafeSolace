package com.example.cafesolace.Pages

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.cafesolace.R
import kotlinx.coroutines.delay

// Updated Dessert model to use imageUrl instead of imageRes
data class Dessert(val name: String, val price: String, val imageUrl: String)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DessertScreen(navController: NavController) {
    // Replace these URLs with your actual Firebase Storage image URLs
    val desserts = listOf(
        Dessert("Espresso", "Rs.700.00", "https://firebasestorage.googleapis.com/v0/b/cafesolace-1.firebasestorage.app/o/expressso.jpg?alt=media&token=063cb27a-5a18-4085-b606-f419a007ef13"),
        Dessert("Latte", "Rs.790.00", "https://firebasestorage.googleapis.com/v0/b/cafesolace-1.firebasestorage.app/o/latte25.jpg?alt=media&token=7721df46-b3f4-4d28-b798-3a17e8a50abd"),
        Dessert("Cappuccino", "Rs.450.00", "https://firebasestorage.googleapis.com/v0/b/cafesolace-1.firebasestorage.app/o/capuchino.jpg?alt=media&token=f8fc6e3d-f822-4d8d-a47a-792610b06c63"),
        Dessert("Mocha", "Rs.500.00", "https://firebasestorage.googleapis.com/v0/b/cafesolace-1.firebasestorage.app/o/mocha.jpg?alt=media&token=6c231ce3-f947-4cdf-b79d-004610a47b50"),
        Dessert("Latte new", "Rs.800.00", "https://firebasestorage.googleapis.com/v0/b/cafesolace-1.firebasestorage.app/o/latte01.jpg?alt=media&token=a6f99063-6421-4e30-afb7-bc0d8e347607"),
        Dessert("Coffee", "Rs.200.00", "https://firebasestorage.googleapis.com/v0/b/your-app-id.appspot.com/o/coffee.jpg?alt=media"),
        Dessert("Mango Juice", "Rs.300.00", "https://firebasestorage.googleapis.com/v0/b/cafesolace-1.firebasestorage.app/o/b.jpg?alt=media&token=2d82756b-9520-49fb-9fd2-39b74c2ff0ca"),
        Dessert("Faluda", "Rs.570.00", "https://firebasestorage.googleapis.com/v0/b/cafesolace-1.firebasestorage.app/o/c.png?alt=media&token=15a628b5-9692-4d92-a9eb-9d4bc4783c6e")
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
            AsyncImage(
                model = dessert.imageUrl,
                contentDescription = dessert.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.z),
                error = painterResource(R.drawable.a)
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
