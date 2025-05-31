import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.*

@Composable
fun rememberUserLocation(context: Context): State<Location?> {
    // Holds the current user location state; initially null until fetched
    val locationState = remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(Unit) {
        // Check if ACCESS_FINE_LOCATION permission is granted
        val isPermissionGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED

        // If permission is not granted, do not proceed with location retrieval
        if (!isPermissionGranted) return@LaunchedEffect

        // Get the fused location provider client for fetching location data
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // Suppress lint warning since permission is already checked above
        @SuppressLint("MissingPermission")
        val task = fusedLocationClient.lastLocation

        // Attach a listener to handle the successful fetching of the last known location
        task.addOnSuccessListener { location: Location? ->
            locationState.value = location  // Update the state with fetched location
        }
    }

    // Return the location state so that callers can reactively observe location updates
    return locationState
}
