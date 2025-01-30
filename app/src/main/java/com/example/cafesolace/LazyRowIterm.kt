package com.example.cafesolace

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FoodCard(
    imageResourceId: Int,
    title: String,
    price: String,
    navController: NavController,
    backgroundColor: Color = Color.White,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp) // Reduced padding slightly for compactness
            .width(170.dp)
            .height(240.dp) // Reduced height slightly to bring elements closer
            .clickable { onClick() }
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),

            ),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(8.dp) // Added padding inside the column
        ) {
            Image(
                painter = painterResource(imageResourceId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(120.dp) // Reduced height of the image to save space
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp), // Updated for more rounded corners
                    ),
            )
            Text(
                text = title,
                fontSize = 16.sp, // Slightly smaller text size for better layout fit
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp) // Reduced padding to minimize space
            )
            Text(
                text = "Rs $price",
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp) // Reduced vertical padding
            )
            Spacer(modifier = Modifier.height(8.dp)) // Added a small spacer for better alignment
            Button(
                onClick = {
                    // Navigate to Master2 screen
                    navController.navigate("master2")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFFFF),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(
                        width = 2.dp, // Thickness of the border
                        color = MaterialTheme.colorScheme.scrim, // Color of the border
                        shape = RoundedCornerShape(12.dp) // Rounded corners with 12.dp radius
                    )
            ) {
                Text(
                    text = "Buy",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.scrim,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

        }

        }
    }

