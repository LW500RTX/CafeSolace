package com.example.cafesolace.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cafesolace.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScreen(navController: NavHostController? = null) {
    val colorScheme = MaterialTheme.colorScheme

    // State to hold user input in the search bar
    var searchText by remember { mutableStateOf("") }

    // Filter the list of outlets based on the search text
    val filteredOutlets = remember(searchText) {
        sampleOutlets.filter {
            it.name.contains(searchText, ignoreCase = true)
        }
    }

    // Scaffold provides the screen structure with a TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Promotions", color = colorScheme.onPrimaryContainer) },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colorScheme.onPrimaryContainer)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primaryContainer
                )
            )
        },
        containerColor = colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(innerPadding)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Search bar to filter outlets
                PromotionSearchBar(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it }
                )

                // Title for the list section
                Text(
                    text = "Top Deals",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorScheme.onBackground,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Dynamically add filtered outlet cards
            items(filteredOutlets) { outlet ->
                OutletCard(outlet)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    // A TextField styled as a search bar with leading icon and placeholder
    TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = { Text("Mains 🍕, Coffee ☕, Doughnut 🍩") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = colorScheme.surfaceVariant,
            focusedTextColor = colorScheme.onSurface,
            unfocusedTextColor = colorScheme.onSurface,
            focusedPlaceholderColor = colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = colorScheme.onSurfaceVariant,
            focusedLeadingIconColor = colorScheme.onSurfaceVariant,
            unfocusedLeadingIconColor = colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = colorScheme.onSurfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
    )
}

// Data class that represents each Outlet item
data class Outlet(
    val name: String,
    val imageRes: Int,
    val rating: String,
    val time: String,
    val fee: String,
    val pickUpOnly: Boolean
)

// Sample hardcoded data list for outlets
val sampleOutlets = listOf(
    Outlet("Cafe Solace (Kandy)", R.drawable.bannermain, "84% (25+)", "22mins", "LKR 750.00", true),
    Outlet("Cafe Solace (Katugasthota)", R.drawable.capuchino, "N/A", "15mins", "LKR 1099.00", true),
    Outlet("Cafe Solace (Mulgampola)", R.drawable.cupcakes, "84% (25+)", "34mins", "LKR 950.00", true),
    Outlet("Cafe Solace (Peradeniya)", R.drawable.italiancuisine, "N/A", "11mins", "LKR 999.00", true),
)

@Composable
fun OutletCard(outlet: Outlet) {
    val colorScheme = MaterialTheme.colorScheme

    // A stylized card for each outlet with image, labels, and info
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Column {
            Box {
                // Image banner
                Image(
                    painter = painterResource(id = outlet.imageRes),
                    contentDescription = outlet.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )

                // Promotional banner if pickup only
                if (outlet.pickUpOnly) {
                    Text(
                        text = "THIS WEEK ONLY",
                        color = colorScheme.onPrimary,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(colorScheme.primary, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                // Outlet name
                Text(outlet.name, fontWeight = FontWeight.Bold, color = colorScheme.onSurface)

                Spacer(modifier = Modifier.height(4.dp))

                // Delivery time and fee
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp), tint = colorScheme.onSurfaceVariant)
                    Text(" Est: ${outlet.time}", modifier = Modifier.padding(start = 4.dp), color = colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Fee: ${outlet.fee}", color = colorScheme.onSurfaceVariant)
                }

                // Rating if available
                outlet.rating.takeIf { it != "N/A" }?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ThumbUp, contentDescription = null, modifier = Modifier.size(16.dp), tint = colorScheme.primary)
                        Text(" $it", modifier = Modifier.padding(start = 4.dp), color = colorScheme.onSurface)
                    }
                }
            }
        }
    }
}
