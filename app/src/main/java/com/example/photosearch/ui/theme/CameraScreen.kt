package com.example.photosearch.ui.theme

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import java.util.concurrent.Executors

@Composable
fun CameraScreen(onObjectDetected: (String) -> Unit) {
    val context = LocalContext.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val imageCapture = remember { ImageCapture.Builder().build() }

    CameraPreview(context, imageCapture, cameraExecutor, onObjectDetected)
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreview(
    context: Context,
    imageCapture: ImageCapture,
    cameraExecutor: java.util.concurrent.ExecutorService,
    onObjectDetected: (String) -> Unit
) {
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder().build().also { analysis ->
                val options = ObjectDetectorOptions.Builder()
                    .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                    .enableMultipleObjects()
                    .enableClassification()
                    .build()

                val objectDetector = ObjectDetection.getClient(options)

                analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                    val rotation = imageProxy.imageInfo.rotationDegrees
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val inputImage = InputImage.fromMediaImage(mediaImage, rotation)
                        objectDetector.process(inputImage)
                            .addOnSuccessListener { results ->
                                for (obj in results) {
                                    val label = obj.labels.firstOrNull()?.text ?: "Objeto"
                                    onObjectDetected(label)
                                    Log.d("PhotoSearch", "Detectado: $label")
                                }
                            }
                            .addOnFailureListener {
                                Log.e("PhotoSearch", "Error en detección", it)
                            }
                            .addOnCompleteListener { imageProxy.close() }
                    } else {
                        imageProxy.close()
                    }
                }
            }

            try {
                val cameraProvider = cameraProviderFuture.get()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    ctx as androidx.lifecycle.LifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalyzer,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("PhotoSearch", "Error iniciando cámara", e)
            }

            previewView
        }
    )
}
