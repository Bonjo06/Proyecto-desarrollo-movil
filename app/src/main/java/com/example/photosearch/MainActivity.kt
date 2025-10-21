package com.example.photosearch

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.photosearch.ui.theme.PhotoSearchTheme
import com.example.photosearch.viewmodel.MainViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions[Manifest.permission.CAMERA] == true &&
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            if (!granted) {
                Toast.makeText(this, "Se necesitan permisos de cámara y ubicación", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionsLauncher.launch(
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        )

        setContent {
            PhotoSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CameraScreen(viewModel = mainViewModel)
                }
            }
        }
    }
}

@Composable
fun CameraScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var imageCapture: ImageCapture? = remember { null }

    Box(modifier = Modifier.fillMaxSize()) {
        // Vista previa de la cámara
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    imageCapture = ImageCapture.Builder().build()

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            }
        )

        // Botón para tomar foto
        Button(
            onClick = {
                val photoFile = File(
                    context.externalCacheDir,
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()) + ".jpg"
                )
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                imageCapture?.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            Toast.makeText(context, "Error al guardar foto: ${exc.message}", Toast.LENGTH_SHORT).show()
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            Toast.makeText(context, "Foto guardada: ${photoFile.name}", Toast.LENGTH_SHORT).show()
                            val uri = Uri.fromFile(photoFile)
                            // Aquí podrías enviar la foto al ViewModel o analizarla con ML Kit
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        ) {
            Text("Tomar Foto")
        }
    }
}

