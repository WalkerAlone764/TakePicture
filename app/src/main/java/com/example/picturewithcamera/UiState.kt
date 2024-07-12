package com.example.picturewithcamera

import android.graphics.Bitmap

data class UiState(
    val imagePath: String? = null
)

sealed interface UiAction {
    data class TakePicture(val bitmap: Bitmap) : UiAction
}
