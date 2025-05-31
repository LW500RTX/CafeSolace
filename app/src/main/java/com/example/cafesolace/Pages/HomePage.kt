package com.example.cafesolace.Pages

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.cafesolace.CommonSection.RoundedItermCart
import com.example.cafesolace.Data.FoodItems
import com.example.cafesolace.Data.RoundedItems
import com.example.cafesolace.FoodCard
import com.example.cafesolace.Model.Main
import com.example.cafesolace.Model.Round
import com.example.cafesolace.R
import kotlinx.coroutines.delay

@Composable
fun MainScreen(navController: NavController) {
    // State to hold the text entered in the search field
    val searchQuery = remember { mutableStateOf("") }

    // Scroll state for vertical scrolling of the entire screen content
    val scrollState = rememberScrollState()

    // Scaffold provides the basic screen structure
    Scaffold(
        modifier = Modifier.fillMaxSize() // Fill entire available screen space
    ) { paddingValues ->  // paddingValues accounts for system bars, navigation, etc.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply scaffold's padding
                .verticalScroll(scrollState), // Enable vertical scrolling
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top header row: "Cafe Solace" text with two colors, and cart icon placeholder
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Build styled text with "Cafe" and "Solace" differently colored
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 34.sp
                            )
                        ) {
                            append("Cafe ")
                        }
                        withStyle(
                            style = SpanStyle(
                                MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold,
                                fontSize = 34.sp
                            )
                        ) {
                            append("Solace")
                        }
                    }
                )

                // You can add a Cart IconButton here if needed (currently not present)
            }

            // Search input box section with leading search icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },  // Update searchQuery state on input
                    label = { Text("Search") },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = "Search Icon")
                    },
                    shape = RoundedCornerShape(26.dp), // Rounded corners for the text field
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(10.dp)) // Spacer for vertical space

            // Banner slideshow showing promotional images
            BannerSlideshow(
                bannerImages = listOf(
                    R.drawable.bannermain, // Drawable resources for banner images
                    R.drawable.banner4,
                    R.drawable.banner3
                ),
                slideshowInterval = 3000L // 3 seconds interval for slideshow
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Section title row for Featured Items with "Show More" button on the right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Featured Items",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )

                // Button to navigate to the full Dessert screen on click
                TextButton(
                    onClick = {
                        navController.navigate("DessertScreen")
                    }
                ) {
                    Text(
                        text = "Show More",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            // Horizontal list of featured food items (limited to 5)
            FooditemsList(foodList = FoodItems().loadFoodItems(), navController = navController)

            Spacer(modifier = Modifier.height(10.dp))

            // Section title for Signature Items
            Text(
                text = "Our Signature Items",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start)
                    .padding(horizontal = 10.dp)
            )

            // Horizontal list of rounded style signature items
            RoundedItermList(RoundedList = RoundedItems().loadRoundedItems())

            Spacer(modifier = Modifier.height(16.dp))

            // Large promotional banner image with rounded corners
            Image(
                painter = painterResource(R.drawable.banner11),
                contentDescription = "Banner11",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(90.dp)) // Extra space at the bottom
        }
    }
}

// Category buttons for filtering (Coffee, Mains, Desserts)
@Composable
fun CategoryButtons() {
    // Track which category is selected
    val selectedCategory = remember { mutableStateOf<String?>(null) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(28.dp) // Spacing between buttons
    ) {
        // Loop through the category list and create buttons
        items(listOf("Coffee", "Mains", "Desserts")) { category ->
            Box {
                OutlinedButton(
                    onClick = {
                        selectedCategory.value = category // Update selected category on click
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategory.value == category) {
                            MaterialTheme.colorScheme.inversePrimary // Highlight selected button
                        } else {
                            Color(0xA4A26854) // Default background for unselected buttons
                        },
                        contentColor = if (selectedCategory.value == category) {
                            MaterialTheme.colorScheme.onSecondaryContainer // Text color when selected
                        } else {
                            MaterialTheme.colorScheme.tertiaryContainer // Text color unselected
                        }
                    ),
                    border = null // No border
                ) {
                    Text(
                        text = category,
                        color = if (selectedCategory.value == category) {
                            MaterialTheme.colorScheme.primary // Text color for selected
                        } else {
                            MaterialTheme.colorScheme.onErrorContainer // Text color for unselected
                        },
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

// Composable to display a single Coffee item using FoodCard
@Composable
fun CoffeIterm(foodPictures: Main, modifier: Modifier, navController: NavController) {
    FoodCard(
        imageResourceId = foodPictures.imageResId,
        title = stringResource(id = foodPictures.name),
        price = stringResource(id = foodPictures.price),
        navController = navController, // For navigation on clicking the food item card
        backgroundColor = MaterialTheme.colorScheme.surface
    )
}

// Horizontal list of featured food items (limited to 5)
@Composable
fun FooditemsList(foodList: List<Main>, navController: NavController) {
    val limitedFooditem = foodList.take(5) // Show only first 5 items
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(limitedFooditem) { fooditem ->
            CoffeIterm(foodPictures = fooditem, modifier = Modifier.padding(9.dp), navController = navController)
        }
    }
}

// Composable to display a rounded signature item card
@Composable
fun RoundedIterm(RoundedItermPictures: Round, modifier: Modifier) {
    RoundedItermCart(
        imageResourceId = RoundedItermPictures.imageResourceId,
        backgroundColor = MaterialTheme.colorScheme.surface
    )
}

// Horizontal list of rounded signature items (limited to 5)
@Composable
fun RoundedItermList(RoundedList: List<Round>) {
    val limitedRoundedIterms = RoundedList.take(5) // Show only first 5 items
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(limitedRoundedIterms) { RoundedIterm ->
            RoundedIterm(RoundedItermPictures = RoundedIterm, modifier = Modifier.padding(9.dp))
        }
    }
}

// Banner slideshow to cycle through promotional images automatically
@Composable
fun BannerSlideshow(
    bannerImages: List<Int>, // Drawable resource IDs of banners
    slideshowInterval: Long = 200L // Time interval between slides (ms)
) {
    // State to track which banner image is currently shown
    var currentIndex by remember { mutableStateOf(0) }

    // Effect that updates currentIndex every slideshowInterval milliseconds
    LaunchedEffect(currentIndex) {
        delay(slideshowInterval) // Wait specified time
        currentIndex = (currentIndex + 1) % bannerImages.size // Cycle to next image (loop back)
    }

    // Display current banner image
    val currentBanner = painterResource(id = bannerImages[currentIndex])
    Image(
        painter = currentBanner,
        contentDescription = "Banner Slideshow Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp)), // Rounded corners for banners
        contentScale = ContentScale.Crop // Crop to fill bounds nicely
    )
}
