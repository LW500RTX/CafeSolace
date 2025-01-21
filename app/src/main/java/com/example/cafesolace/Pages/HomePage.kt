package com.example.cafesolace.Pages

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import com.example.cafesolace.CommonSection.RoundedItermCart
import com.example.cafesolace.Data.FoodItems
import com.example.cafesolace.Data.RoundedItems
import com.example.cafesolace.FoodCard
import com.example.cafesolace.Model.Main
import com.example.cafesolace.Model.Round
import com.example.cafesolace.R
import com.example.cafesolace.ui.theme.AuthViewModel


@Composable
fun MainScreen() {
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
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFFFF9800),
                                fontWeight = FontWeight.Bold,
                                fontSize = 34.sp
                            )
                        ) {
                            append("Cafe ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.Red,
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
            Image(
                painter = painterResource(R.drawable.bannermain),
                contentDescription = "Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Buttons Section
            CategoryButtons()

            Spacer(modifier = Modifier.height(10.dp))

            // Featured Items Section
            Text(
                text = "Featured Items",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold,fontSize = 20.sp), // Apply bold style
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start)
            )


            FooditemsList(foodList = FoodItems().loadFoodItems())
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
            .padding(horizontal = 0.dp), // Padding for the entire LazyRow
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
                            Color(0xFFC99954) // Highlight color when selected
                        } else {
                            Color(0xA4A26854) // Default background color
                        },
                        contentColor = if (selectedCategory.value == category) {
                            Color.White // Text color for selected
                        } else {
                            Color.Yellow // Text color for unselected
                        }
                    ),
                    border = null // Remove or customize the border
                ) {
                    Text(
                        text = category,
                        color = if (selectedCategory.value == category) {
                            Color.Black // Text color for selected
                        } else {
                            Color.White // Text color for unselected
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
fun CoffeIterm(foodPictures: Main, modifier: Modifier) {
    FoodCard(
        imageResourceId = foodPictures.imageResId,
        title = stringResource(id = foodPictures.name),
        price = stringResource(id = foodPictures.price),
        backgroundColor = Color.White
    )
}

@Composable
fun FooditemsList(foodList: List<Main>) {
    val limitedFooditem = foodList.take(5)
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(limitedFooditem) { fooditem ->
            CoffeIterm(foodPictures = fooditem, modifier = Modifier.padding(9.dp))
        }
    }
}


@Composable
fun RoundedIterm(RoundedItermPictures: Round,modifier: Modifier) {
    RoundedItermCart(
        imageResourceId = RoundedItermPictures.imageResourceId,
        backgroundColor = Color.White
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
