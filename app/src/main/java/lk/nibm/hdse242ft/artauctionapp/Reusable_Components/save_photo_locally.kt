package lk.nibm.hdse242ft.artauctionapp.Reusable_Components


// ---------------------------
// Utility function to save photo locally
// ---------------------------
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.net.URL

suspend fun downloadAndSaveImage(context: Context, imageUrl: String?, fileName: String): String? {
    if (imageUrl.isNullOrEmpty()) return null
    return try {
        val url = URL(imageUrl)
        val bitmap = BitmapFactory.decodeStream(url.openStream())
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        file.absolutePath  // return local file path
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}




