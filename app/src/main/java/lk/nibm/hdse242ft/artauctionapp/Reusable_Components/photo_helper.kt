package lk.nibm.hdse242ft.artauctionapp.Reusable_Components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import java.io.File
import androidx.compose.ui.layout.ContentScale
import android.util.Base64
import java.io.InputStream
@Composable
fun ProfilePhotoPicker(
    context: Context,
    imageUri: Uri?,
    placeholder: Painter,
    onImageSelected: (Uri) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedUri by remember { mutableStateOf(imageUri) }
    val cameraUriState = remember { mutableStateOf<Uri?>(null) }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraUriState.value?.let {
                selectedUri = it
                onImageSelected(it)
            }
        }
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // Permission granted → launch camera
            cameraUriState.value?.let { cameraLauncher.launch(it) }
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedUri = it
            onImageSelected(it)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = selectedUri?.let { rememberAsyncImagePainter(it) } ?: placeholder,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop, // ← add this
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                .clickable { showDialog = true }
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Tap to add profile photo",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select Image") },
            text = { Text("Choose Camera or Gallery") },
            confirmButton = {
                TextButton(onClick = {
                    val imagesDir = File(context.cacheDir, "profile_images").apply { mkdirs() }
                    val file = File(imagesDir, "profile_${System.currentTimeMillis()}.jpg")
                    val cameraUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        file
                    )
                    cameraUriState.value = cameraUri

                    // Request permission → only launch camera if granted
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    showDialog = false
                }) { Text("Camera") }
            },
            dismissButton = {
                TextButton(onClick = {
                    galleryLauncher.launch("image/*")
                    showDialog = false
                }) { Text("Gallery") }
            }
        )
    }
}


fun uriToBase64(context: Context, uri: Uri): String {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val bytes = inputStream?.readBytes() ?: ByteArray(0)
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}

