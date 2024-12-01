package com.jetbrains.kmpapp.screens.stock.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.colors.bottomBarSelectedItemColor
import com.jetbrains.kmpapp.colors.grayChip
import com.jetbrains.kmpapp.colors.greenIcon
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.User


@Composable
fun ElevatedButtonSyncProducts(
    enabled : Boolean,
    context: Context,
    statusLogin: SessionManager<User>,
    syncProducts:()->Unit,
    changeEnabled:(Boolean)->Unit)
{
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
        ElevatedAssistChip(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(30.dp),
            enabled = enabled,
            onClick = {
                changeEnabled(false)
                when (statusLogin) {
                    is SessionManager.Offline -> {
                        Toast.makeText(
                            context,
                            "Imposible sincronizar en modo Offline",
                            Toast.LENGTH_SHORT
                        ).show()
                        changeEnabled(true)
                    }

                    is SessionManager.Online -> {
                        syncProducts()
                        changeEnabled(true)
                    }
                }
            },
            label = { Text("Sincronizar todo", color = greenIcon) },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = bottomBarSelectedItemColor
            ),
            border = AssistChipDefaults.assistChipBorder(borderColor = grayChip, enabled = true),
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Sync,
                        contentDescription = "Sincronizar todo",
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            }
        )
    }
}