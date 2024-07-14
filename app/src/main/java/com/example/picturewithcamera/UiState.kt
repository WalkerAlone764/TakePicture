package com.example.picturewithcamera

import android.net.Uri

data class UiState(
    val imagePath: String? = null
)

sealed interface UiAction {
    data class TakePicture(val file: Uri) : UiAction
}
