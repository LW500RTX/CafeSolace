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
import com.example.cafesolace.CommonSection.Desserts
import com.example.cafesolace.CommonSection.MainsCard

import com.example.cafesolace.CommonSection.RoundedItermCart
import com.example.cafesolace.Data.DessertPItems
import com.example.cafesolace.Data.FoodItems
import com.example.cafesolace.Data.MainPItems

import com.example.cafesolace.Data.RoundedItems
import com.example.cafesolace.FoodCard
import com.example.cafesolace.Model.DessertP
import com.example.cafesolace.Model.Main
import com.example.cafesolace.Model.MainsP

import com.example.cafesolace.Model.Round
import com.example.cafesolace.R

@Composable
fun ProductScreen() {
    val searchQuery = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp),
                horizontalArrangement = Arrangement.End, // Align to the right
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Handle view cart logic */ },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "View Cart")
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Center, // Center the content horizontally
                verticalAlignment = Alignment.CenterVertically // Align the content vertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Normal,
                                fontSize = 30.sp
                            )
                        ) {
                            append("Find Your Favourite Meal ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = 34.sp
                            )
                        ) {
                            append("")
                        }
                    }
                )
            }


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



//            Image(
//                painter = painterResource(R.drawable.bannermain),
//                contentDescription = "Banner",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(270.dp)
//                    .padding(4.dp)
//                    .clip(RoundedCornerShape(16.dp)),
//                contentScale = ContentScale.Crop
//            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Trending",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp, // Change font size
                    fontWeight = FontWeight.Bold, // Set font weight
                    color = Color.Black // Set text color
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start)
            )

            RoundedItermList(RoundedList = RoundedItems().loadRoundedItems())
            Spacer(modifier = Modifier.height(10.dp))


            Text(
                text = "MAINS",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp, // Change font size
                    fontWeight = FontWeight.Bold, // Set font weight
                    color = Color.Black // Set text color
                ),

                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start)
            )

//           FooditemsList(foodList = FoodItems().loadFoodItems())
//            Spacer(modifier = Modifier.height(10.dp))


            MainspItermsList(MainspList = MainPItems().loadMainItems())

            Text(
                text = "DESSERTS",
                style = MaterialTheme.typography.titleMedium.copy( // Modify the typography style
                    fontSize = 18.sp, // Change font size
                    fontWeight = FontWeight.Bold, // Set font weight
                    color = Color.Black // Set text color
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start)
            )


            DessertItermList(MainspList = DessertPItems().loadDessertItems())

//            Image(
//                painter = painterResource(R.drawable.banner11),
//                contentDescription = "Banner11",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(220.dp)
//                    .padding(4.dp)
//                    .clip(RoundedCornerShape(16.dp)),
//                contentScale = ContentScale.Crop
//            )
        }
    }
}

@Composable
fun MainsPIterm(MainsPictures: MainsP, modifier: Modifier) {
    MainsCard(
        imageResourceId = MainsPictures.imageResId,
        title = stringResource(id = MainsPictures.name),
        price = stringResource(id = MainsPictures.price),
        backgroundColor = Color.White
    )
}

@Composable
fun MainspItermsList(MainspList: List<MainsP>) {
    val MainsplimitedFooditem = MainspList.take(6)
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(MainsplimitedFooditem) { MainsItermsnew ->
            MainsPIterm(MainsPictures = MainsItermsnew, modifier = Modifier.padding(9.dp))

        }
    }
}

@Composable
fun DessertPIterm(DessertPictures: DessertP, modifier: Modifier) {
    Desserts(
        imageResourceId = DessertPictures.imageResId,
        title = stringResource(id = DessertPictures.name),
        price = stringResource(id = DessertPictures.price),
        backgroundColor = Color.White
    )
}

@Composable
fun DessertItermList(MainspList: List<DessertP>) {
    val MainsplimitedFooditem = MainspList.take(6)
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(MainsplimitedFooditem) { MainsItermsnew ->
            DessertPIterm(DessertPictures = MainsItermsnew, modifier = Modifier.padding(9.dp))

        }
    }
}
