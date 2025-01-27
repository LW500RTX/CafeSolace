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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cafesolace.Model.Main

@Composable
fun MasterView(
    main: Main,
    navController: NavController,
){
    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        startAnimation = true
    }
    val context = LocalContext.current

    val offsetX by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 3000.dp,
        animationSpec = tween(durationMillis = 400)
    )
    var quantity by remember { mutableStateOf(1) }
    var rating by remember { mutableStateOf(0) }
    val name = stringResource(id = main.name)
    val price = main.price
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = offsetX)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(35.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(vertical = 12.dp),
        ) {
            Spacer(modifier = Modifier.height(35.dp))

            // Product Image
            Image(
                painter = painterResource(id = main.imageResId),
                contentDescription = stringResource(id = main.name),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = main.name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp
                )


            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "by",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "John Doe",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$${price}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "captures the serene and mysterious beauty of the cosmos. The artwork portrays a vast expanse of deep, velvety darkness punctuated by countless twinkling stars.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                (1..5).forEach { index ->
                    Icon(
                        imageVector = if (index < rating) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Star Rating",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                rating = index
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { if (quantity > 1) quantity-- }) {
                    Text(text = "-", fontSize = 24.sp)
                }

                Text(
                    text = "$quantity",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )

                TextButton(onClick = { quantity++ }) {
                    Text(text = "+", fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}