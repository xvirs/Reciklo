package com.jetbrains.kmpapp.utils

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts


fun openMultiplePickerGalleryUseCase(multiplePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>>) {
    multiplePhotoPickerLauncher.launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    )
}

fun openSingularPickerGallery(singularPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, @JvmSuppressWildcards Uri?>) {
    singularPickerLauncher.launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    )
}