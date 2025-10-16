package com.example.photosearch

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.photosearch.ui.theme.PhotoSearchTheme
import com.example.photosearch.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    // 1. Obtenemos la instancia del ViewModel, igual que antes.
    private val mainViewModel: MainViewModel by viewModels()

    // 2. Preparamos el lanzador para solicitar permisos.
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "El permiso de ubicación es necesario.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificamos y solicitamos el permiso al crear la actividad.
        checkAndRequestLocationPermission()

        setContent {
            PhotoSearchTheme {
                // Surface es un contenedor básico en Compose.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 3. Llamamos a nuestra pantalla principal, pasándole el ViewModel.
                    MainScreen(viewModel = mainViewModel)
                }
            }
        }
    }

    private fun checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    // 4. Observamos el LiveData del ViewModel y lo convertimos en un "Estado" de Compose.
    //    Cada vez que el LiveData cambie, la UI se "recompondrá" (redibujará) automáticamente.
    val result by viewModel.photoResult.observeAsState()
    val context = LocalContext.current

    // LaunchedEffect se usa para ejecutar código que no es parte de la UI (como mostrar un Toast)
    // cuando un estado cambia. Se ejecutará cada vez que 'result' tenga un nuevo valor.
    LaunchedEffect(result) {
        result?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    // Box es un layout que permite apilar elementos.
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // Centramos el contenido.
    ) {
        Button(
            // 5. En el onClick, simplemente llamamos a la función del ViewModel.
            //    La UI no sabe qué pasa, solo notifica la acción.
            onClick = { viewModel.onPhotoButtonPressed("Objeto desde Compose") },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Tomar Foto y Obtener Ubicación")
        }
    }
}