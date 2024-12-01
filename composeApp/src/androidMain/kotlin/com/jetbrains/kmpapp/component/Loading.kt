package com.jetbrains.kmpapp.component


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun Loading(color : Color? = Color.Transparent) {
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(10.dp)
                .graphicsLayer {
                    alpha = 0.5f
                }
                .background(color!!)
        ) {
            DotsLoadingIndicator()
        }
    }

}

@Composable
fun DotsLoadingIndicator() {
    val transition = rememberInfiniteTransition(label = "")
    val dot1Offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val dot2Offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, delayMillis = 300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val dot3Offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, delayMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,

        ) {
        Dot(offsetY = dot1Offset)
        Spacer(modifier = Modifier.width(8.dp))
        Dot(offsetY = dot2Offset)
        Spacer(modifier = Modifier.width(8.dp))
        Dot(offsetY = dot3Offset)
    }
}


@Composable
fun Dot(offsetY: Float) {
    Box(
        modifier = Modifier
            .offset(y = offsetY.dp)
            .size(16.dp)
            .background(Color(0xFF296A48), shape = CircleShape)
    )
}
