package com.jetbrains.kmpapp.component.addProductDataScreen.galleryImageList

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jetbrains.kmpapp.R
import com.jetbrains.kmpapp.component.ShimmerAnimationOptimized
import com.jetbrains.kmpapp.component.visible

@Composable
fun ItemCardGallery(uri: Uri, deleteSelected: (Uri) -> Unit, showPreview: (Uri) -> Unit ,presentation:Boolean, isVisible: Boolean) {

    val context = LocalContext.current
    var statusLoading by remember { mutableStateOf(true) }

    Box(modifier = if (!presentation) Modifier.containerSmall() else Modifier.containerBig()) {
        ShimmerAnimationOptimized(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .visible(statusLoading))
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(uri)
                .crossfade(true)
                .listener(
                    onSuccess = {request, result -> statusLoading = false  },
                    onError = {request, result -> statusLoading = false}
                )
                .error(R.drawable.icon_sinsincornizar)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    showPreview(uri)
                },
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.padding(5.dp).align(Alignment.TopEnd).visible(isVisible)) {
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .background(MaterialTheme.colors.background, shape = CircleShape)
                    .clickable { deleteSelected(uri) }
            ) {
                Icon(
                    imageVector = Icons.Default.Remove, contentDescription = "Remove Photo",
                )
            }
        }
    }
}


private fun Modifier.containerSmall() = composed {
    height(100.dp)
        .width(100.dp)
        .padding(horizontal = 6.dp)
}

private fun Modifier.containerBig() = composed {
    height(300.dp)
        .width(300.dp)
        .padding(horizontal = 6.dp)
}

