package com.example.cafesolace.Pages

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cafesolace.Data.RoundedItems
import com.example.cafesolace.Pages.RoundedItermList
import com.example.cafesolace.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import com.google.maps.android.compose.rememberCameraPositionState

// Suspended function to fetch JSON string from a remote URL using OkHttp
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

// Suspended function to parse the JSON string and extract UI strings as a map
suspend fun parseUiStrings(jsonString: String): Map<String, String> {
    return try {
        val jsonObject = JSONObject(jsonString)
        val uiStrings = jsonObject.getJSONObject("ui_strings")
        val map = mutableMapOf<String, String>()
        uiStrings.keys().forEach { key -> map[key] = uiStrings.getString(key) }
        map
    } catch (e: Exception) {
        e.printStackTrace()
        emptyMap()
    }
}

// Attempt to load UI strings from remote JSON URL
suspend fun loadRemoteStrings(): Map<String, String> {
    val jsonString = fetchJsonFromUrl("https://raw.githubusercontent.com/LW500RTX/CafeSolaceJsonData/refs/heads/main/strings.json")
    return if (jsonString != null) parseUiStrings(jsonString) else emptyMap()
}

// Fallback function to load UI strings from local assets JSON file
suspend fun loadLocalStrings(context: Context): Map<String, String> = withContext(Dispatchers.IO) {
    try {
        val inputStream = context.assets.open("strings.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        parseUiStrings(jsonString)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyMap()
    }
}

// Loads UI strings preferring remote source, falls back to local asset if remote fails
suspend fun loadStringsWithFallback(context: Context): Map<String, String> {
    val remoteStrings = loadRemoteStrings()
    return if (remoteStrings.isEmpty()) loadLocalStrings(context) else remoteStrings
}

@Composable
fun Master2Screen(navController: NavController) {
    val context = LocalContext.current
    // State holding UI strings map loaded from JSON
    val strings = remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    // States for user interaction
    var quantity by remember { mutableStateOf(1) }   // Quantity selector
    var isSpicy by remember { mutableStateOf(false) } // Toggle switch state
    var showDialog by remember { mutableStateOf(false) } // Show/hide dialog
    var isVisible by remember { mutableStateOf(false) }  // Controls visibility animations

    // Load strings asynchronously when composable enters composition
    LaunchedEffect(Unit) {
        strings.value = loadStringsWithFallback(context)
        isVisible = true // Trigger animations after strings load
    }

    // Location of coffee shop for map (Kandy, Sri Lanka)
    val coffeeShopLocation = LatLng(7.2906, 80.6337)
    // Camera state to control Google Map view
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(coffeeShopLocation, 15f)
    }

    // Remember scroll state for vertical scrolling
    val scrollState = rememberScrollState()

    // Main screen layout with vertical scroll enabled
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top row with back button
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = strings.value["back_button_description"] ?: ""
                )
            }
        }

        // Animated visibility for the coffee image with fade and scale-in effect
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(5000)) + scaleIn(initialScale = 0.8f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.capuchino),
                contentDescription = strings.value["image_description"] ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Animated product title and rating slide in vertically
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { 900 }) + fadeIn()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = strings.value["brand_name"] ?: "",
                    color = Color(0xFFDAA520),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = strings.value["product_name"] ?: "",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = strings.value["rating_text"] ?: "",
                        fontSize = 18.sp,
                        color = Color(0xFFFFA500)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Animated description text slide in horizontally
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(initialOffsetX = { -100 }) + fadeIn()
        ) {
            Text(
                text = strings.value["description_text"] ?: " ",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Google Map section showing coffee shop location with fade-in animation
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(1000))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    // Marker for coffee shop location
                    Marker(
//                        position = coffeeShopLocation,
                        title = strings.value["brand_name"] ?: "Café Solace"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Signature items list with title and custom composable RoundedItermList
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(4200))
        ) {
            Column {
                Text(
                    text = strings.value["signature_items_title"] ?: "",
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

        // Toggle switch for "Spicy" option with label
        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn(initialScale = 0.8f) + fadeIn()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = strings.value["toggle_label"] ?: "",
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

        // Quantity selector with decrement and increment buttons including scaling animation on increment
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            var scale by remember { mutableStateOf(1f) }

            Button(
                onClick = { if (quantity > 1) quantity-- },  // Decrease quantity with lower limit 1
                modifier = Modifier.animateContentSize()
            ) {
                Text("-")
            }

            Text(text = "$quantity", fontSize = 18.sp, modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    quantity++          // Increase quantity
                    scale = 1.1f        // Trigger scale animation effect
                },
                modifier = Modifier
                    .animateContentSize()
                    .graphicsLayer(scaleX = scale, scaleY = scale)
            ) {
                Text("+")
            }

            // Reset scale animation after delay when quantity changes
            LaunchedEffect(quantity) {
                delay(1150)
                scale = 1f
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add to Cart button with fade-in animation and custom color
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(800))
        ) {
            Button(
                onClick = { showDialog = true },  // Show confirmation dialog on click
                colors = ButtonDefaults.buttonColors(Color(0xFFE0821B)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = strings.value["add_to_cart_button"] ?: "",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }

    // Confirmation dialog for adding to cart
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = strings.value["dialog_title"] ?: "") },
            text = {
                Text(strings.value["dialog_text"] ?: "")
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text(strings.value["dialog_confirm"] ?: "")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(strings.value["dialog_cancel"] ?: "")
                }
            }
        )
    }
}
