package com.example.photosearch

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.photosearch.ui.theme.CameraScreen
import com.example.photosearch.ui.theme.HistoryScreen
import com.example.photosearch.ui.theme.PhotoSearchTheme
import com.example.photosearch.viewmodel.MainViewModel

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

        // 🔹 Pedir permisos al iniciar
        requestPermissionsLauncher.launch(
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        )

        setContent {
            PhotoSearchTheme {
                var currentScreen by remember { mutableStateOf("camera") }
                val context = LocalContext.current

                Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
                    when (currentScreen) {
                        "camera" -> CameraScreen { detectedLabel ->
                            mainViewModel.onPhotoButtonPressed(detectedLabel)
                            Toast.makeText(context, "Detectado: $detectedLabel", Toast.LENGTH_SHORT).show()
                            currentScreen = "history" // Cambia a historial tras guardar
                        }

                        "history" -> HistoryScreen(
                            photoList = mainViewModel.photoList.collectAsState().value,
                            onBack = { currentScreen = "camera" }
                        )
                    }
                }
            }
        }
    }
}
