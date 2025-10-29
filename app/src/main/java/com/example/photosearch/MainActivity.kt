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
import com.example.photosearch.repository.UserRepository
import com.example.photosearch.ui.theme.*
import com.example.photosearch.viewmodel.MainViewModel
import kotlinx.coroutines.launch

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

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            mainViewModel.onPhotoButtonPressed("Imagen seleccionada", it.toString())
            Toast.makeText(this, "Imagen seleccionada y guardada", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🟢 Solicita permisos al iniciar
        requestPermissionsLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

        setContent {
            PhotoSearchTheme {
                val context = LocalContext.current
                val userRepository = remember { UserRepository(context) }
                val coroutineScope = rememberCoroutineScope()

                var currentScreen by remember { mutableStateOf<String?>(null) }
                var loggedUser by remember { mutableStateOf<com.example.photosearch.data.UserEntity?>(null) }

                // 🔹 Verifica si ya hay usuario guardado
                LaunchedEffect(Unit) {
                    val existingUser = userRepository.getUser()
                    loggedUser = existingUser
                    currentScreen = if (existingUser == null) "register" else "home"
                }

                // 🧭 Navegación entre pantallas
                if (currentScreen == null) {
                    Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
                        Text("Cargando...")
                    }
                } else {
                    Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
                        when (currentScreen) {

                            // 🧾 Pantalla de registro
                            "register" -> RegisterScreen(
                                onRegisterDone = {
                                    Toast.makeText(context, "Usuario registrado ✅", Toast.LENGTH_SHORT).show()
                                    coroutineScope.launch {
                                        loggedUser = userRepository.getUser()
                                        currentScreen = "home"
                                    }
                                }
                            )

                            // 🏠 Pantalla principal (Home)
                            "home" -> loggedUser?.let { user ->
                                HomeScreen(
                                    user = user,
                                    onOpenCamera = { currentScreen = "camera" },
                                    onOpenHistory = { currentScreen = "history" },
                                    onLogout = {
                                        coroutineScope.launch {
                                            context.deleteDatabase("photo_db")
                                            Toast.makeText(context, "Sesión cerrada ✅", Toast.LENGTH_SHORT).show()
                                            loggedUser = null
                                            currentScreen = "register"
                                        }
                                    }
                                )
                            }

                            // 📷 Pantalla de cámara (con botón de volver)
                            "camera" -> CameraScreen(
                                onCapture = { detectedLabel, imagePath ->
                                    mainViewModel.onPhotoButtonPressed(detectedLabel, imagePath)
                                    Toast.makeText(context, "Detectado: $detectedLabel", Toast.LENGTH_SHORT).show()
                                    currentScreen = "history"
                                },
                                onPickImage = {
                                    pickImageLauncher.launch("image/*")
                                },
                                onBack = { currentScreen = "home" } // 🔙 Botón de volver
                            )

                            // 🖼️ Historial de fotos detectadas
                            "history" -> HistoryScreen(
                                photoList = mainViewModel.photoList.collectAsState().value,
                                onBack = { currentScreen = "home" }
                            )
                        }
                    }
                }
            }
        }
    }
}
