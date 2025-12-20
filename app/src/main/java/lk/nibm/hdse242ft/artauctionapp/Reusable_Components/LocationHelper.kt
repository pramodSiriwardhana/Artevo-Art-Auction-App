package lk.nibm.hdse242ft.artauctionapp.Reusable_Components


import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    context: Context,
    onLocationReceived: (address: String, latitude: Double, longitude: Double) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = CurrentLocationRequest.Builder()
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setMaxUpdateAgeMillis(0)
        .build()

    fusedLocationClient.getCurrentLocation(locationRequest, null)
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
                try {
                    val geocoder = android.location.Geocoder(context)
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val readableAddress = if (!addresses.isNullOrEmpty()) {
                        addresses[0].locality ?: addresses[0].subAdminArea ?: addresses[0].adminArea ?: "Unknown"
                    } else "Location not found"

                    onLocationReceived(readableAddress, location.latitude, location.longitude)

                } catch (e: Exception) {
                    e.printStackTrace()
                    onLocationReceived("Unable to get address", 0.0, 0.0)
                }
            } else {
                onLocationReceived("Unable to get location", 0.0, 0.0)
            }
        }
        .addOnFailureListener {
            onLocationReceived("Error: ${it.message}", 0.0, 0.0)
        }
}

@Composable
fun LocationPermissionButton(
    context: Context,
    onLocationReceived: (address: String, latitude: Double, longitude: Double) -> Unit, // 3 params
    content: @Composable (onClick: () -> Unit) -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                getCurrentLocation(context, onLocationReceived) // now matches
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        // Nothing to auto-launch yet
    }

    content {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            getCurrentLocation(context, onLocationReceived)
        } else {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}

