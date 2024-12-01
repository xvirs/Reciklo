package com.jetbrains.kmpapp.component.addProductDataScreen.cameraScreen

import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner

@Composable
fun CameraPreview(
    cameraController: LifecycleCameraController,
    lifeCycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PreviewView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.MAGENTA)
                scaleType = PreviewView.ScaleType.FILL_START
            }.also { previewView ->
                previewView.controller = cameraController
                cameraController.bindToLifecycle(lifeCycleOwner)
            }
        })
}