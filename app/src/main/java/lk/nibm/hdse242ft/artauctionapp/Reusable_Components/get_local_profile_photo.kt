package lk.nibm.hdse242ft.artauctionapp.Reusable_Components




import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import android.content.Context
import java.io.File

/**
 * Returns the local file path of the user's profile photo if it exists,
 * or null if not found.
 */
fun getLocalProfilePhoto(context: Context, userId: Int): String? {
    val file = File(context.filesDir, "profile_$userId.jpg")
    return if (file.exists()) file.absolutePath else null
}

//@Composable
//fun LocalProfileImage(
//    context: Context,
//    userId: Int,
//    placeholder: Painter,
//    modifier: Modifier = Modifier
//) {
//    val localPath = getLocalProfilePhoto(context, userId)
//    val painter = if (localPath != null) {
//        rememberAsyncImagePainter(File(localPath))
//    } else placeholder
//
//    Image(
//        painter = painter,
//        contentDescription = "Profile Image",
//        modifier = modifier,
//        contentScale = ContentScale.Crop
//    )
//}
