package com.example.picturewithcamera

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.picturewithcamera.ui.theme.PictureWithCameraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PictureWithCameraTheme {
                var bitmap: Bitmap? by remember {
                    mutableStateOf(null)
                }

                val viewModel by viewModels<MainViewmodel>()
                val uiState by viewModel.uiState.collectAsState()

                val takePicture =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
                        try {
                            bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                it.data?.extras?.getParcelable(
                                    "data",
                                    Bitmap::class.java
                                )
                            } else {
//                                it.data?.extras?.get("data") as Bitmap
                                it.data?.getParcelableExtra("data")
                            }
                            if (bitmap != null) {
                                viewModel.onAction(UiAction.TakePicture(bitmap!!))
                            }

//
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        TextButton(onClick = {
                            takePicture.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                        }) {
                            Text(text = "Take Picture")
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        if (uiState.imagePath != null) {
                            AsyncImage(
                                model = uiState.imagePath,
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}