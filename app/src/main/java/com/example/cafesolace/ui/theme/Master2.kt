package com.example.cafesolace.Pages

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.example.cafesolace.Data.RoundedItems
import com.example.cafesolace.Pages.RoundedItermList
import com.example.cafesolace.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

suspend fun fetchJsonFromUrl(url: String): String? = withContext(Dispatchers.IO) {
    try {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            response.body?.string()
        } else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun loadRemoteStrings(): Map<String, String> {
    val jsonString = fetchJsonFromUrl("https://raw.githubusercontent.com/LW500RTX/CafeSolaceJsonData/refs/heads/main/strings.json")
    return if (jsonString != null) {
        val jsonObject = JSONObject(jsonString)
        val map = mutableMapOf<String, String>()
        jsonObject.keys().forEach { key ->
            map[key] = jsonObject.getString(key)
        }
        map
    } else emptyMap()
}

@Composable
fun Master2Screen(navController: NavController) {
    val context = LocalContext.current
    val strings = remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    var quantity by remember { mutableStateOf(1) }
    var isSpicy by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        strings.value = loadRemoteStrings()
        isVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F2E8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = strings.value["back_button_description"] ?: "Go Back")
            }
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(5000)) + scaleIn(initialScale = 0.8f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.capuchino),
                contentDescription = strings.value["image_description"] ?: "Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { 900 }) + fadeIn()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = strings.value["brand_name"] ?: "Cafe Solace",
                    color = Color(0xFFDAA520),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = strings.value["product_name"] ?: "Espresso",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = strings.value["rating_text"] ?: "⭐ 4.9",
                        fontSize = 18.sp,
                        color = Color(0xFFFFA500)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(initialOffsetX = { -100 }) + fadeIn()
        ) {
            Text(
                text = strings.value["description_text"]
                    ?: "At Cafe Solace, we believe in more than just coffee—we craft moments of peace, comfort, and connection.",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(4200))
        ) {
            Column {
                Text(
                    text = strings.value["signature_items_title"] ?: "Our Signature Items",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.Start)
                        .padding(horizontal = 10.dp)
                )
                RoundedItermList(RoundedList = RoundedItems().loadRoundedItems())
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn(initialScale = 0.8f) + fadeIn()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = strings.value["toggle_label"] ?: "Whipped Cream",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Switch(
                    checked = isSpicy,
                    onCheckedChange = { isSpicy = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.Red)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            var scale by remember { mutableStateOf(1f) }

            Button(
                onClick = { if (quantity > 1) quantity-- },
                modifier = Modifier.animateContentSize()
            ) {
                Text("-")
            }

            Text(text = "$quantity", fontSize = 18.sp, modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    quantity++
                    scale = 1.1f
                },
                modifier = Modifier
                    .animateContentSize()
                    .graphicsLayer(scaleX = scale, scaleY = scale)
            ) {
                Text("+")
            }

            LaunchedEffect(quantity) {
                delay(1150)
                scale = 1f
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(800))
        ) {
            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(Color(0xFFE0821B)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = strings.value["add_to_cart_button"] ?: "Add to Cart - Rs. 600",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = strings.value["dialog_title"] ?: "Confirm Add to Cart") },
            text = { Text(strings.value["dialog_text"] ?: "Are you sure you want to add this item to your cart?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text(strings.value["dialog_confirm"] ?: "Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text(strings.value["dialog_cancel"] ?: "No")
                }
            }
        )
    }
}
