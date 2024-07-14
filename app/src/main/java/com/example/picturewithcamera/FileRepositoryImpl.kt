package com.example.picturewithcamera

import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FileRepository {
    override suspend fun convertBitmapToFileAndSaveToAppStore(bitmap: Bitmap): File? {
        return withContext(Dispatchers.IO) {
            try {
                val tempFile = File(context.filesDir, "/temp_${System.currentTimeMillis()}.jpeg")
                context.contentResolver.openOutputStream(tempFile.toUri())?.use { outStream ->
                    bitmap.compress(
                        Bitmap.CompressFormat.JPEG, 100, outStream
                    )
                }
                tempFile
            } catch (e: Exception) {
                null
            }
        }
    }
}