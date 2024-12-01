package com.jetbrains.kmpapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController


//fun requestPermission(context: Context) {
//    ActivityCompat.requestPermissions(
//        context as ComponentActivity,
//        CameraUtils.REQUIRED_PERMISSIONS,
//        CameraUtils.REQUEST_CODE_PERMISSIONS
//    )
//}

fun allPermissionsGranted(context: Context) =
    CameraUtils.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
data class CameraUtils(val tag: String) {

    companion object Permission {
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}
enum class TypeCamera{
    DOCUMENT1,
    DEMERIT,
    DEFAULT
}
