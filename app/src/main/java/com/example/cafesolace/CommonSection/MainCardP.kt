package com.example.cafesolace.CommonSection

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainsCard(
    imageResourceId: Int,
    title: String,
    price: String,
    backgroundColor: Color = Color.White,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp) // Reduced padding slightly for compactness
            .width(170.dp)
            .height(190.dp) // Reduced height slightly to bring elements closer
            .clickable { onClick() }
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color.Black,
                spotColor = Color.Black
            ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(8.dp) // Added padding inside the column
        ) {
            Image(
                painter = painterResource(imageResourceId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(110.dp) // Reduced height of the image to save space
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(10.dp), // Updated for more rounded corners
                        ambientColor = Color.Black,
                        spotColor = Color.Black
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
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp) // Reduced vertical padding
            )
            Spacer(modifier = Modifier.height(8.dp)) // Added a small spacer for better alignment


        }

    }
}