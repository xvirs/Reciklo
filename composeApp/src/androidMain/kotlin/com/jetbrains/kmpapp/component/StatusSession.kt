package com.jetbrains.kmpapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.colors.grayChip

@Composable
fun StatusSession(status: String, color: Color, colorIcon :Color, imageVector: ImageVector) {
    AssistChip(
        shape = RoundedCornerShape(30.dp),
        onClick = {},
        border = AssistChipDefaults.assistChipBorder(borderColor = grayChip, enabled = true),
        label = { Text(status) },
        leadingIcon = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color)
                    .size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = imageVector,
                    tint = colorIcon,
                    contentDescription = "Estado de app",
                    modifier = Modifier.size(AssistChipDefaults.IconSize)
                )
            }
        }
    )
}