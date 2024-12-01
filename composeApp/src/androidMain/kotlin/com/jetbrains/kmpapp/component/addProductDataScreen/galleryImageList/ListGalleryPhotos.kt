package com.jetbrains.kmpapp.component.addProductDataScreen.galleryImageList

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.utils.openMultiplePickerGalleryUseCase

@Composable
fun ListGalleryPhotos(listUris: List<Uri>, onSelectImages:(Uri)->Unit,deletePhotoSelected:(Uri)->Unit, setShowDialogImagePreview:(Uri)->Unit) {
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            uris.onEach {
                onSelectImages(it)
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        contentAlignment = if (listUris.isEmpty()) Alignment.Center else Alignment.TopStart
    ) {
        LazyRow(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = if (listUris.isEmpty()) Arrangement.Center else Arrangement.Start
        ) {
            items(listUris.size) { index ->
                ItemCardGallery(uri = listUris[index], deleteSelected = deletePhotoSelected, {
                    setShowDialogImagePreview(it)
                }, presentation = false, isVisible = true)
            }

            item {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color(0xF2000000),
                    modifier = Modifier
                        .clickable {
                            openMultiplePickerGalleryUseCase(multiplePhotoPickerLauncher)
                        }
                        .padding(25.dp)
                )
            }
        }
    }
}
