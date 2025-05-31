package com.example.cafesolace.CommonSection

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MainsCard(
    imageResourceId: Int,               // Resource ID for the dish image
    title: String,                      // Title of the dish
    price: String,                      // Price of the dish as a string
    backgroundColor: Color = Color.White, // Background color of the card, default is white
    modifier: Modifier = Modifier,          // Modifier for styling from the caller
    onClick: () -> Unit = {},               // Optional onClick lambda (not used here as nav handled internally)
    navController: NavController            // Navigation controller to handle navigation on click
) {
    // Obtain the current configuration to detect device orientation
    val configuration = LocalConfiguration.current
    // Boolean flag: true if device is in landscape orientation
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Determine card width based on orientation
    val cardWidth = if (isLandscape) 220.dp else 170.dp
    // Determine card height based on orientation
    val cardHeight = if (isLandscape) 160.dp else 190.dp
    // Determine image height inside the card based on orientation
    val imageHeight = if (isLandscape) 90.dp else 110.dp

    // Main Card composable that represents a single dish item
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)  // Add padding around the card
            .width(cardWidth)                              // Set card width
            .height(cardHeight)                            // Set card height
            .clickable {
                // When the card is clicked, navigate to detailed product view screen
                // Passing imageResourceId as a parameter to the route
                navController.navigate("detailedProductView/$imageResourceId")
            }
            .shadow(                                       // Apply shadow with elevation and rounded corners
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color.Black,
                spotColor = Color.Black
            ),
        // Set the background color of the card container
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        // Use a vertical column to arrange image and texts
        Column(
            modifier = Modifier.padding(8.dp)  // Padding inside the card content
        ) {
            // Display the image of the dish
            Image(
                painter = painterResource(imageResourceId),
                contentDescription = null,       // Image is decorative; no description needed for accessibility
                contentScale = ContentScale.Fit, // Maintain aspect ratio fitting inside bounds
                modifier = Modifier
                    .height(imageHeight)         // Set image height based on orientation
                    .fillMaxWidth()              // Image fills the width of the card
                    .shadow(                    // Apply shadow to image for depth
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp),
                        ambientColor = Color.Black,
                        spotColor = Color.Black
                    ),
            )
            // Display the title of the dish
            Text(
                text = title,
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .fillMaxWidth()              // Title fills the card width
                    .padding(top = 4.dp)        // Top padding between image and title
            )
            // Display the price of the dish
            Text(
                text = "Rs $price",
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,  // Semi-bold font for emphasis
                modifier = Modifier
                    .fillMaxWidth()              // Price text fills card width
                    .padding(vertical = 4.dp)   // Vertical padding above and below price
            )
            // Add a spacer for some bottom space inside the card
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
