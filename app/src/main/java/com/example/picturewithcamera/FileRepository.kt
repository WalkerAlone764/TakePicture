package com.example.picturewithcamera

import android.graphics.Bitmap
import java.io.File

interface FileRepository {

    suspend fun convertBitmapToFileAndSaveToAppStore(bitmap: Bitmap): File?
}