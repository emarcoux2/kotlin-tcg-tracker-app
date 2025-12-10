package com.example.tcgtracker.ui.screens
//
//import android.content.Intent
//import android.util.Log
//import androidx.annotation.OptIn
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ExperimentalGetImage
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.Preview
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.defaultMinSize
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.wrapContentSize
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.core.content.ContextCompat
//import androidx.core.net.toUri
//import androidx.lifecycle.compose.LocalLifecycleOwner
//import androidx.navigation.NavController
//import com.example.tcgtracker.components.CameraPreview
//import com.example.tcgtracker.components.ScreenLabel
//import com.example.tcgtracker.components.scanEnums.ScanMode
//import com.example.tcgtracker.components.scanEnums.ScanType
//
///**
// * This screen allows a logged-in user to scan a physical card using the device camera.
// *
// * @param navController - The object responsible for navigation between composable screens.
// */
//@OptIn(ExperimentalGetImage::class)
//@Composable
//fun ScannerScreen(
//    initialMode: ScanMode,
//    onScanned: (ScanType, String) -> Unit,
//    onCancel: () -> Unit
//) {
//    // gives Android context needed for starting activities or accessing system services
//    val context = LocalContext.current
//
//    // used by CameraX to bind the camera to the lifecycle
//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    // current scanning mode
//    var mode by remember { mutableStateOf(initialMode) }
//
//    // whether scanning is currently active or not
//    var isScanning by remember { mutableStateOf(true) }
//
//    // holds scan results temporarily until they're handled
//    var pendingResult by remember { mutableStateOf<String?>(null) }
//    var pendingType by remember { mutableStateOf<ScanType?>(null) }
//
//    // runs side effects in Compose when the variables change
//    LaunchedEffect(pendingResult, pendingType) {
//        if (pendingResult != null && pendingType == ScanType.TEXT) {
//            onScanned(ScanType.TEXT, pendingResult!!)
//            pendingResult = null
//            pendingType = null
//            isScanning = true
//        }
//    }
//
//    CameraPreview { previewView, cameraProvider ->
//        // stores the timestamp of the last scan to throttle scanning
//        // to avoid multiple scans per second
//        var lastScanTime = 0L
//
//        // builds a CameraX preview use case
//        // connects the preview to the UI (PreviewView) so the camera feed is displayed
//        val preview = Preview.Builder().build().also {
//            it.setSurfaceProvider(previewView.surfaceProvider)
//        }
//
//        // builds an image analysis use case
//        // STRATEGY_KEEP_ONLY_LATEST processes the latest frame only, avoids backlogs
//        val analysis = ImageAnalysis.Builder()
//            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//            .build()
//
//        // sets a function to analyze each camera frame
//        // uses getMainExecutor so it runs on the main thread
//        analysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
//            // prevents scanning while a previous scan is still processing
//            if (!isScanning) {
//                imageProxy.close()
//                return@setAnalyzer
//            }
//
//            // skip analysis if scanning is paused
//            // ensures at least 1 second between scans to prevent multiple triggers
//            val now = System.currentTimeMillis()
//            if (now - lastScanTime < 1000) {
//                imageProxy.close()
//                return@setAnalyzer
//            }
//            lastScanTime = now
//
//            // converts the raw camera frame into an InputImage for ML Kit
//            val media = imageProxy.image
//            val rotation = imageProxy.imageInfo.rotationDegrees
//            val input = media?.let { InputImage.fromMediaImage(it, rotation) }
//                ?: run {
//                    imageProxy.close()
//                    return@setAnalyzer
//                }
//
//            when (mode) {
//                ScanMode.QR -> {
//
//                    // sends the frame to ML Kit for QR Code scanning
//                    // if a QR code is found, it stops scanning and stores the result
//                    // in pendingResult / pendingType
//                    BarcodeScanning.getClient()
//                        .process(input)
//                        .addOnSuccessListener { codes ->
//                            val barcode = codes.firstOrNull() ?: return@addOnSuccessListener
//                            val url = barcode.url?.url
//                            val raw = barcode.rawValue
//
//                            val content = url ?: raw ?: return@addOnSuccessListener
//
//                            isScanning = false
//
//                            pendingResult = content
//                            pendingType = ScanType.QR
//                        }
//                        .addOnFailureListener { e ->
//                            Log.e("Scanner", "QR scan failed", e)
//                        }
//                        .addOnCompleteListener {
//                            // imageProxy must be closed after processing
//                            // otherwise frames will backlog
//                            imageProxy.close()
//                        }
//                }
//
//                ScanMode.TEXT -> {
//
//                    // sends the frame to ML Kit for text recognition
//                    TextRecognition.getClient(
//                        TextRecognizerOptions.Builder().build()
//                    )
//                        .process(input)
//                        .addOnSuccessListener { text ->
//                            if (text.text.isNotBlank()) {
//                                isScanning = false
//                                pendingResult = text.text
//                                pendingType = ScanType.TEXT
//                            }
//                        }
//                        .addOnFailureListener { e ->
//                            Log.e("Scanner", "Text scan failed", e)
//                        }
//                        .addOnCompleteListener {
//                            imageProxy.close()
//                        }
//                }
//            }
//        }
//
//        try {
//            // starts the camera and binds preview + analysis to the composable's lifecycle
//            // stops the camera when the screen is closed
//            cameraProvider.bindToLifecycle(
//                lifecycleOwner,
//                CameraSelector.DEFAULT_BACK_CAMERA,
//                preview,
//                analysis
//            )
//        } catch (e: Exception) {
//            Log.e("ScanningScreen", "Camera bind failed", e)
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .align(Alignment.Center)
//                .wrapContentSize()
//                .background(Color(0x66000000))
//                .padding(8.dp)
//        ) {
//            Text(text = if (mode == ScanMode.QR) "Scan a QR code" else "Scan some text")
//        }
//
//        Row(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(16.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Box(modifier = Modifier.fillMaxSize()) {
//                Button(
//                    onClick = { mode = if (mode == ScanMode.QR) ScanMode.TEXT else ScanMode.QR },
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(bottom = 30.dp)
//                        .defaultMinSize(minWidth = 160.dp, minHeight = 56.dp)
//                ) {
//                    Text(if (mode == ScanMode.QR) "Scan Text" else "Scan QR Code")
//                }
//            }
//            if (pendingResult != null && pendingType == ScanType.QR) {
//                AlertDialog(
//                    onDismissRequest = {},
//                    title = { Text("Scan result") },
//                    text = { Text(pendingResult!!) },
//                    confirmButton = {
//                        TextButton(onClick = {
//                            val intent = Intent(Intent.ACTION_VIEW, pendingResult?.toUri())
//                            context.startActivity(intent)
//
//                            pendingResult = null
//                            pendingType = null
//                            isScanning = true
//                        }) {
//                            Text("Open Link")
//                        }
//                    },
//                    dismissButton = {
//                        TextButton(onClick = {
//                            pendingResult = null
//                            pendingType = null
//                            isScanning = true
//                        }) {
//                            Text("Scan Again")
//                        }
//                    }
//                )
//            }
//        }
//
//        FloatingActionButton(
//            onClick = { onCancel() },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(end = 16.dp, bottom = 30.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                contentDescription = "Back"
//            )
//        }
//    }
//}