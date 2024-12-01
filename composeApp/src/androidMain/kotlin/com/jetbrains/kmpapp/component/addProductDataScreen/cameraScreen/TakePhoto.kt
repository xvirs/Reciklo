package com.jetbrains.kmpapp.component.addProductDataScreen.cameraScreen

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import java.io.File


fun takePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    showDialog: (Uri) -> Unit,
    fileName: String? = null
) {

    val file = if (fileName.equals("DOCUMENT1")){
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        File(directory, "$fileName.jpeg")
    } else {
        File.createTempFile("reciklo", ".jpeg")
    }

    val outputDirectory = ImageCapture.OutputFileOptions.Builder(file).build()
    val mainExecutor = ContextCompat.getMainExecutor(context)
    cameraController.takePicture(
        outputDirectory,
        mainExecutor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                outputFileResults.savedUri?.let {
                    showDialog(it)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Camera", "error para sacar la imagen", exception)
            }
        })
}