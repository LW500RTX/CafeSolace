package com.example.cafesolace

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FoodCard(
    imageResourceId: Int,           // Resource ID for the image to display in the card
    title: String,                  // Title of the food item
    price: String,                  // Price of the food item
    navController: NavController,   // Navigation controller to handle navigation on button click
    backgroundColor: Color = Color.White, // Background color of the card, default is white
    modifier: Modifier = Modifier,  // Modifier to customize the card layout externally
    onClick: () -> Unit = {}        // Optional onClick lambda to handle card click events
) {
    // Get current screen configuration to check orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Adjust card dimensions and font size based on orientation
    val cardWidth = if (isLandscape) 220.dp else 170.dp
    val cardHeight = if (isLandscape) 200.dp else 240.dp
    val imageHeight = if (isLandscape) 100.dp else 120.dp
    val fontSize = if (isLandscape) 14.sp else 16.sp

    // Card container with padding, fixed width/height, clickable, and shadow effect
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .width(cardWidth)
            .height(cardHeight)
            .clickable { onClick() }      // Calls the passed in onClick lambda when card is clicked
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor) // Set card background color
    ) {
        // Vertical layout inside the card
        Column(
            modifier = Modifier.padding(8.dp)  // Padding inside card content
        ) {
            // Food item image with fixed height and full width, shadowed with rounded corners
            Image(
                painter = painterResource(imageResourceId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(imageHeight)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(8.dp),
                    ),
            )
            // Title text below the image
            Text(
                text = title,
                fontSize = fontSize,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
            // Price text with semi-bold font weight
            Text(
                text = "Rs $price",
                fontSize = fontSize,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp)) // Spacer to separate price and button

            // Buy button centered horizontally with border and white background
            Button(
                onClick = {
                    // Navigate to the screen named "master2" on button click
                    navController.navigate("master2")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFFFF),  // White background for button
                    contentColor = Color.White            // White content color (overridden by Text style)
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterHorizontally) // Center the button horizontally
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.scrim, // Border color from theme scrim
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                // Text inside the button styled with scrim color and bold font weight
                Text(
                    text = "Buy",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.scrim,
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
