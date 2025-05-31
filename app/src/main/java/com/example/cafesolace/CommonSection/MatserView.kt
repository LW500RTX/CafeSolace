package com.example.cafesolace.CommonSection

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cafesolace.Model.Main

@Composable
fun MasterView(
    main: Main,                      // The data model object representing the product/item
    navController: NavController,    // Navigation controller to handle screen navigation (not used here but can be extended)
){
    // State to control animation start
    var startAnimation by remember { mutableStateOf(false) }

    // Launch effect to trigger the animation as soon as composable is composed
    LaunchedEffect(Unit) {
        startAnimation = true
    }

    // Get current context (could be used for things like Toasts, not used here)
    val context = LocalContext.current

    // Get device screen configuration
    val configuration = LocalConfiguration.current

    // Screen width in dp units
    val screenWidth = configuration.screenWidthDp.dp

    // Animate the horizontal offset of the entire Box composable
    // Starts from 3000.dp (off screen to right) and animates to 0.dp (on screen)
    val offsetX by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 3000.dp,
        animationSpec = tween(durationMillis = 400)
    )

    // Remember and maintain quantity state with default value 1
    var quantity by remember { mutableStateOf(1) }

    // Remember and maintain rating state with default value 0 (no stars selected)
    var rating by remember { mutableStateOf(0) }

    // Get the localized string for the product name from resources
    val name = stringResource(id = main.name)

    // Price from the model, expected to be a number or string
    val price = main.price

    // Root container box that fills the screen, applies horizontal offset animation and background color
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = offsetX)                 // Apply horizontal animation offset (should be x, but code uses y)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(35.dp)) // Add top space (though this is inside a Box with fillMaxSize, might have no effect)

        // Horizontal row that holds the image and details side by side, scrollable horizontally if needed
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(vertical = 12.dp)
                .horizontalScroll(rememberScrollState()), // Enable horizontal scrolling for this row
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.width(35.dp)) // Left side padding space

            // Display the product image
            Image(
                painter = painterResource(id = main.imageResId),  // Load image resource from the model
                contentDescription = stringResource(id = main.name), // Accessibility description
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))  // Rounded corners for image
                    .height(250.dp)                    // Fixed height
                    .width(250.dp),                    // Fixed width
                contentScale = ContentScale.Crop          // Crop image to fill bounds maintaining aspect ratio
            )

            Spacer(modifier = Modifier.width(16.dp)) // Space between image and text details

            // Column holding product textual details and interactive elements
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
            ) {
                // Product name/title
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(8.dp)) // Space after title

                // Row displaying "by Lalan Weerasooriy" (author or creator)
                Row {
                    Text(
                        text = "by",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Lalan Weerasooriy",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // Space after author

                // Price text, formatted with a dollar sign
                Text(
                    text = "$${price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(8.dp)) // Space after price

                // Description text of the product, hardcoded here (could be made dynamic)
                Text(
                    text = "captures the serene and mysterious beauty of the cosmos. The artwork portrays a vast expanse of deep, velvety darkness punctuated by countless twinkling stars.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp)) // Space before rating stars

                // Row for star rating display and selection
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Display 5 stars, filled or outlined depending on current rating
                    (1..5).forEach { index ->
                        Icon(
                            imageVector = if (index <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Star Rating",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    // When a star is clicked, update the rating accordingly
                                    rating = index
                                }
                        )
                        Spacer(modifier = Modifier.width(4.dp)) // Space between stars
                    }
                }

                Spacer(modifier = Modifier.height(16.dp)) // Space before quantity controls

                // Row containing quantity selector controls: decrement, quantity display, increment
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Button to decrement quantity (not allowing below 1)
                    TextButton(onClick = { if (quantity > 1) quantity-- }) {
                        Text(text = "-", fontSize = 24.sp)
                    }

                    // Display the current quantity
                    Text(
                        text = "$quantity",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    // Button to increment quantity
                    TextButton(onClick = { quantity++ }) {
                        Text(text = "+", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp)) // Small space after quantity controls
                }
            }
        }
    }
}
