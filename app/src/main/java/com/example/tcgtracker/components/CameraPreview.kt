package com.example.tcgtracker.components

import android.util.Log
import android.view.ViewGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
// onViewReady - creates the view and waits for the camera provider asynchronously
// PreviewView - a special Android view from CameraX - displays what the camera sees
// ProcessCameraProvider - camera manager for CameraX, handles binding camera use cases
// and lifecycle awareness
fun CameraPreview(onViewReady: (PreviewView, ProcessCameraProvider) -> Unit) {
    // without the Context, we cannot create the views or access the system resources
    // that PreviewView and ProcessCameraProvider need
    val context = LocalContext.current

    // compose function that allows us to embed traditional Android Views in Jetpack Compose
    AndroidView(

        // defines how to create the Android View when Compose needs it
        factory = { ctx ->

            // creates a CameraX preview view to show the camera feed
            // ctx is the Context passed in by Compose
            PreviewView(ctx).apply {

                // sets the view to match the parent's width and height
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },

        // defines what to do every time the view is updated
        update = { previewView ->
            // returns a ListenableFuture<ProcessCameraProvider> - an async promise that
            // will eventually provide the ProcessCameraProvider
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            // listener that will run when the camera provider is ready
            cameraProviderFuture.addListener({
                val cameraProvider = try {
                    cameraProviderFuture.get()
                } catch (e: Exception) {
                    Log.e("CameraPreview", "CameraProvider error", e)
                    return@addListener
                }

                // tells the caller that the camera preview view and camera provider
                // are ready, and the camera can be set up now
                onViewReady(previewView, cameraProvider)

                // runs the listener on the main thread, needed for updating the UI
            }, ContextCompat.getMainExecutor(context))
        }
    )
}