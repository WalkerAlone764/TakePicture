package com.example.picturewithcamera

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.picturewithcamera.ui.theme.PictureWithCameraTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Objects

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

                val context = LocalContext.current
                val tempFile = File.createTempFile("temperor", ".jpeg", externalCacheDir)

                val uri = FileProvider.getUriForFile(
                    Objects.requireNonNull(context),
                    "com.example.picturewithcamera" + ".provider", tempFile
                )

                var capturedImageUri by remember {
                    mutableStateOf<Uri>(Uri.EMPTY)
                }

                val cameraLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { result ->
                        if (result) {
                            viewModel.onAction(UiAction.TakePicture(uri))
                        }

                    }

                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) {
                    if (it) {
                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                        cameraLauncher.launch(uri)
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                }

                LaunchedEffect(key1 = true) {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        TextButton(onClick = {
//                            takePicture.launch(
//                                Intent(
//                                    MediaStore.ACTION_IMAGE_CAPTURE,
//                                )
//                            )
                            cameraLauncher.launch(uri)
                        }) {
                            Text(text = "Take Picture")
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        if (uiState.imagePath != null) {
                            AsyncImage(
                                model = uiState.imagePath,
                                contentDescription = "Captured Image"
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}