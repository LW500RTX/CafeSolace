import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.example.cafesolace.Data.RoundedItems
import com.example.cafesolace.Pages.RoundedItermList
import com.example.cafesolace.R

@Composable
fun Master2Screen(navController: NavController) {
    var quantity by remember { mutableStateOf(1) }
    var isSpicy by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F2E8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.capuchino), // Replace with actual image
            contentDescription = "Chicken Burger",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Cafe Solace", color = Color(0xFFDAA520), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Espresso", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "⭐ 4.9", fontSize = 18.sp, color = Color(0xFFFFA500))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "At cafe Solace, we believe in more than just coffee- we craft moments of peace,comfort and connection.Nestled in the heart of Kandy,Our cafe offers a serene retreat from the bustle of everyday life.",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth() // Fills the available width
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
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

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Whipped Cream", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Switch(
                checked = isSpicy,
                onCheckedChange = { isSpicy = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.Red)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { if (quantity > 1) quantity-- }) { Text("-") }
            Text(text = "$quantity", fontSize = 18.sp, modifier = Modifier.padding(16.dp))
            Button(onClick = { quantity++ }) { Text("+") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add to Cart Button
        Button(
            onClick = { showDialog = true }, // Show dialog when clicked
            colors = ButtonDefaults.buttonColors(Color(0xFFE0821B)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add to Cart - Rs. 600", color = Color.White, fontSize = 18.sp)
        }
    }

    // Confirmation Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Confirm Add to Cart")
            },
            text = {
                Text("Are you sure you want to add this item to your cart?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Logic for adding to cart goes here
                        showDialog = false // Close dialog on confirm
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false // Close dialog on cancel
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}


