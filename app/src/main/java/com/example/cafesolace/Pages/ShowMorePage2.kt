package com.example.cafesolace.Pages

// Importing necessary Compose and Android packages
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cafesolace.R

// Main composable function that renders the featured drinks page
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturedDrinksPage(navController: NavController) {

    // List of featured desserts with name, price, and image URL
    val featuredDesserts = listOf(
        Dessert("Ice cream (Perfect bowl for serving creamy, delicious ice cream treats quickly.)", "Rs.450.00", "https://firebasestorage.googleapis.com/v0/b/cafesolace-1.firebasestorage.app/o/icecream.jpg?alt=media&token=baaa5d5e-f729-4c0d-a4b9-ecc8ec088bd0"),
        Dessert("Raspberry tart (Buttery crust filled with sweet, tangy fresh raspberries and cream.)", "Rs.990.00", "https://firebasestorage.googleapis.com/v0/b/cafesolace-1.firebasestorage.app/o/raspberrytart.jpg?alt=media&token=50e094db-c359-4e4f-aef0-469eba011024"),
        Dessert("Macaron's (Delicate, colorful almond cookies with smooth, flavorful creamy fillings inside.)", "Rs.400.00", "https://firebasestorage.googleapis.com/v0/b/cafesolace-1.firebasestorage.app/o/macarons.jpg?alt=media&token=aa2310b2-f9db-42ff-b005-c6622c183e7e"),
        Dessert("Berry with whipped cream cake (Light cake topped with fresh berries and fluffy whipped cream.)", "Rs.580.00", "https://firebasestorage.googleapis.com/v0/b/cafesolace-1.firebasestorage.app/o/berrywithwhippedcreamcake.jpg?alt=media&token=bb36d80a-7576-4da8-a0ab-48b43926a373"),
        Dessert("cup cakes (Small, moist cakes topped with creamy frosting and sweet decorations.)", "Rs.800.00", "https://firebasestorage.googleapis.com/v0/b/cafesolace-1.firebasestorage.app/o/cupcakes.jpg?alt=media&token=ab5f9f52-7253-4cef-ba63-9c89d107aff1")
    )

    // Scaffold provides the basic screen layout structure including top bar
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Cafe Solace Special",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                navigationIcon = {
                    // Back navigation icon
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        // LazyColumn for displaying a scrollable list of featured desserts
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // Iterating through the dessert list
            items(featuredDesserts.size) { index ->
                FeaturedCard(dessert = featuredDesserts[index])
            }
        }
    }
}

// Composable that displays an individual dessert card
@Composable
fun FeaturedCard(dessert: Dessert) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)), // Rounded corners for a soft card design
        elevation = CardDefaults.cardElevation(6.dp), // Card shadow
        colors = CardDefaults.cardColors(containerColor = colors.surface) // Card background color
    ) {
        // Horizontal layout to arrange image and text side by side
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // AsyncImage for loading image from URL
            AsyncImage(
                model = dessert.imageUrl,
                contentDescription = dessert.name,
                placeholder = painterResource(R.drawable.bannermain), // Shown while loading
                error = painterResource(R.drawable.a), // Shown if error occurs
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)) // Image with rounded edges
            )

            Spacer(modifier = Modifier.width(16.dp)) // Space between image and text

            Column(modifier = Modifier.weight(1f)) {
                // Dessert name
                Text(
                    dessert.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = colors.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Dessert price
                Text(
                    dessert.price,
                    color = colors.onSurfaceVariant,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // "Order Now" button
                Button(
                    onClick = { /* Handle order click event */ },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                ) {
                    Text("Order Now", color = colors.onPrimary)
                }
            }
        }
    }
}
