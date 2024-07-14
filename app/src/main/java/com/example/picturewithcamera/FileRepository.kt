package com.example.picturewithcamera

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

interface FileRepository {
    suspend fun convertBitmapToFileAndSaveToAppStore(bitmap: Bitmap): File?

    suspend fun saveFileToAppStore(uri: Uri): File?
}