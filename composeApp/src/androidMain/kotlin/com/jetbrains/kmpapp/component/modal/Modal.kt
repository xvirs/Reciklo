package com.jetbrains.kmpapp.component.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.jetbrains.kmpapp.colors.redLogout

@Composable
fun Modal(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String = "",
    onConfirmButtonText: String = "Salir",
    onDismissButtonText: String = "Continuar"
) {
    AlertDialog(
        modifier = Modifier.padding(32.dp),
        icon = {
            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(100f)).background(redLogout)) {
                Icon(Icons.AutoMirrored.Outlined.ExitToApp, modifier = Modifier.align(Alignment.Center), contentDescription = null)
            }
        },
        title = {
            Text(modifier = Modifier.fillMaxWidth(), text = dialogTitle, textAlign = TextAlign.Center)
        },
        text = {
            if (dialogText.isNotEmpty()) {
                Text(modifier = Modifier.fillMaxWidth(),text = dialogText, textAlign = TextAlign.Center)
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Column {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TextButton(
                        modifier = Modifier.padding(bottom = 8.dp),
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Text(
                            text = onDismissButtonText,
                            color = Color(0xFF296A48) // 296A48
                        )
                    }
                }
                HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(horizontal = 8.dp))
            }
        },
        dismissButton = {
            Column {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TextButton(
                        onClick = {
                            onConfirmation()
                        }
                    ) {
                        Text(
                            text = onConfirmButtonText,
                            color = Color(0xFFBA1A1A) // BA1A1A
                        )
                    }

                }
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    )
}
