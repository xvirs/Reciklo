package com.jetbrains.kmpapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jetbrains.kmpapp.R
import com.jetbrains.kmpapp.colors.white
import com.jetbrains.kmpapp.domain.models.ProductToRender

@Composable
fun AutoParteItem(product: ProductToRender) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        colors = CardDefaults.cardColors(white),
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Show the image
            Box(
                modifier = Modifier
                    .height(89.dp)
                    .width(114.dp)
                    .padding(vertical = 6.dp)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium)
            ) {
                GetImagePoster(product.imageUrl)
            }

            Column(modifier = Modifier.padding(start = 20.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Column {
                    Text(
                        text = if(product.id == null){
                            "RUDAC: ${product.rudac}"
                        } else {
                            "ID: ${product.id}"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = "Marca: ${product.manufacturer}",
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Text(
                        text = "Precio: ${product.price}",
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    }
}



@Composable
fun GetImagePoster(product:String?) {
    val context = LocalContext.current
    var statusLoading by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical=12.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
            ShimmerAnimationOptimized(modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .visible(statusLoading))
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(product)
                .crossfade(true)
                .listener(
                    onSuccess = {request, result -> statusLoading = false  },
                    onError = {request, result -> statusLoading = false}
                )
                .error(R.drawable.icon_sinsincornizar)
                .build(),
            contentDescription = "Image",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

fun Modifier.visible(visible :Boolean): Modifier =this.then(
    if (visible){
        Modifier
    }else{
        Modifier.layout{_, constraints ->
            layout(0,0){}
        }
    }
)