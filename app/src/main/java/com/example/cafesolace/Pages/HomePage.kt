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
fun MainScreen(navController:NavController) {
    val searchQuery = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize() // Ensures full screen coverage
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Using paddingValues for content padding
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Welcome Text with "Cafe" and "Solace" styled separately and Cart icon on the right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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

                IconButton(
                    onClick = { /* Handle view cart logic */ },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "View Cart")
                }
            }

            // Search Box Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    label = { Text("Search") },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = "Search Icon")
                    },
                    shape = RoundedCornerShape(26.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Banner Section
            BannerSlideshow(
                bannerImages = listOf(
                    R.drawable.bannermain, // Replace with your actual drawable resources
                    R.drawable.banner4,
                    R.drawable.banner3 // Add more banners as needed
                ),
                slideshowInterval = 3000L // Set the desired interval (e.g., 3000ms = 3 seconds)
            )


//            Spacer(modifier = Modifier.height(10.dp))
//
//            // Category Buttons Section
//            CategoryButtons()

            Spacer(modifier = Modifier.height(10.dp))

            // Featured Items Section
            Text(
                text = "Featured Items",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold,fontSize = 20.sp), // Apply bold style
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start)
                    .padding(horizontal = 10.dp)
            )


            FooditemsList(foodList = FoodItems().loadFoodItems(), navController = navController)

            Spacer(modifier = Modifier.height(10.dp))

            // Signature Items Section
            Text(
                text = "Our Signature Items",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp // Set the desired font size here
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start)
                    .padding(horizontal = 10.dp)
            )

            RoundedItermList(RoundedList = RoundedItems().loadRoundedItems())
            Spacer(modifier = Modifier.height(16.dp))

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
            Spacer(modifier = Modifier.height(90.dp))
        }
    }


}

@Composable
fun CategoryButtons() {
    // State to keep track of the selected category
    val selectedCategory = remember { mutableStateOf<String?>(null) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp), // Padding for the entire LazyRow
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(28.dp) // Increased space between buttons
    ) {
        items(listOf("Coffee", "Mains", "Desserts")) { category ->
            Box {
                OutlinedButton(
                    onClick = {
                        selectedCategory.value = category // Update the selected category
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .padding(horizontal = 4.dp), // Additional padding inside the button
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategory.value == category) {
                           MaterialTheme.colorScheme.inversePrimary // Highlight color when selected
                        } else {
                            Color(0xA4A26854) // Default background color
                        },
                        contentColor = if (selectedCategory.value == category) {
                            MaterialTheme.colorScheme.onSecondaryContainer // Text color for selected
                        } else {
                            MaterialTheme.colorScheme.tertiaryContainer // Text color for unselected
                        }
                    ),
                    border = null // Remove or customize the border
                ) {
                    Text(
                        text = category,
                        color = if (selectedCategory.value == category) {
                            MaterialTheme.colorScheme.primary // Text color for selected
                        } else {
                            MaterialTheme.colorScheme.onErrorContainer // Text color for unselected
                        },
                        fontSize = 16.sp, // Custom font size
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) // Bold text style
                    )
                }
            }
        }
    }
}




@Composable
fun CoffeIterm(foodPictures: Main, modifier: Modifier, navController: NavController) {
    FoodCard(
        imageResourceId = foodPictures.imageResId,
        title = stringResource(id = foodPictures.name),
        price = stringResource(id = foodPictures.price),
        navController = navController, // Pass navController here
        backgroundColor = MaterialTheme.colorScheme.surface
    )
}


@Composable
fun FooditemsList(foodList: List<Main>, navController: NavController) {
    val limitedFooditem = foodList.take(5)
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(limitedFooditem) { fooditem ->
            CoffeIterm(foodPictures = fooditem, modifier = Modifier.padding(9.dp), navController = navController)
        }
    }
}



@Composable
fun RoundedIterm(RoundedItermPictures: Round,modifier: Modifier) {
    RoundedItermCart(
        imageResourceId = RoundedItermPictures.imageResourceId,
        backgroundColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
fun RoundedItermList(RoundedList: List <Round>){
    val limitedRoundedIterms = RoundedList.take(5)
    LazyRow (modifier = Modifier.fillMaxWidth()){
        items(limitedRoundedIterms) {RoundedIterm ->
            RoundedIterm( RoundedItermPictures = RoundedIterm, modifier = Modifier.padding(9.dp))
        }
    }
}


@Composable
fun BannerSlideshow(
    bannerImages: List<Int>, // List of drawable resource IDs
    slideshowInterval: Long = 200L // Time between slides in milliseconds
) {
    // Track the current image index
    var currentIndex by remember { mutableStateOf(0) }

    // Automatically update the image index
    LaunchedEffect(currentIndex) {
        delay(slideshowInterval) // Wait for the specified interval
        currentIndex = (currentIndex + 1) % bannerImages.size // Cycle through images
    }

    // Display the current banner
    val currentBanner = painterResource(id = bannerImages[currentIndex])
    Image(
        painter = currentBanner,
        contentDescription = "Banner Slideshow Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop
    )
}

