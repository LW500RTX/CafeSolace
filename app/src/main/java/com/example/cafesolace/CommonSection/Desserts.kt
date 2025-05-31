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

@Composable
fun Desserts(
    imageResourceId: Int,             // Image resource ID for the dessert photo
    title: String,                   // Dessert title/name to display
    price: String,                   // Dessert price as string (e.g. "350")
    backgroundColor: Color = Color.White,  // Optional background color (default is white)
    modifier: Modifier = Modifier,   // Optional Modifier for further customization
    onClick: () -> Unit = {}         // Lambda invoked on card click, default is no-op
) {
    // Obtain current screen orientation (portrait or landscape)
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Adjust card size depending on orientation for better responsive UI
    val cardWidth = if (isLandscape) 220.dp else 170.dp
    val cardHeight = if (isLandscape) 160.dp else 190.dp
    val imageHeight = if (isLandscape) 90.dp else 110.dp

    // Card container with shadow, rounded corners, clickable behavior, and padding
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .width(cardWidth)
            .height(cardHeight)
            .clickable { onClick() } // Call onClick when user taps the card
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
            ),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(8.dp) // Padding inside the card content
        ) {
            // Dessert image displayed with fitting scale, rounded shadow, and fixed height
            Image(
                painter = painterResource(imageResourceId),
                contentDescription = null, // decorative image, so no description needed
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(imageHeight)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp),
                    ),
            )
            // Dessert title text styled with labelMedium typography and spacing
            Text(
                text = title,
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
            // Dessert price text with semi-bold weight and spacing for emphasis
            Text(
                text = "Rs $price",
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            // Spacer to add vertical breathing room at bottom of the card
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
