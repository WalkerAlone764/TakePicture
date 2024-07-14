package com.example.picturewithcamera

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: UiAction) {
        when (action) {
            is UiAction.TakePicture -> takePicture(action.file)
        }
    }


    private fun takePicture(tempUri: Uri) {
        viewModelScope.launch {
            val file = fileRepository.saveFileToAppStore(uri = tempUri)
            Log.d("File", file?.path.toString())
            _uiState.emit(
                uiState.value.copy(
                    imagePath = file?.path
                )
            )

        }
    }


}