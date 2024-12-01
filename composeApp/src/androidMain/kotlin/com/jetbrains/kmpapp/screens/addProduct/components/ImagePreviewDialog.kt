package com.jetbrains.kmpapp.screens.addProduct.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import com.jetbrains.kmpapp.R
import com.jetbrains.kmpapp.component.Loading

@Composable
fun ImagePreviewDialog(
    showDialogI: ((Boolean) -> Boolean)? = null,
    imageUri: Uri?,
    onAccept: (() -> Unit?)? = null,
    onDecline: (() -> Unit?)? = null,
    onDismiss: (() -> Unit)
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                onDismiss()
            }
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        SubcomposeAsyncImage(
                            model = imageUri,
                            contentDescription = "Captured image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(25.dp))
                                .background(
                                    Color(0xFFD1E8D6),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(3.dp),
                            loading = {
                                Box(modifier = Modifier.fillMaxWidth(1f).height(510.dp)){
                                    Loading()
                                }
                            },
                            error = {
                                Text("Error loading image", modifier = Modifier.align(Alignment.Center))
                            }
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        if(onDecline != null && onAccept != null ){
                            CustomButtons(onDecline = {onDecline()}, onAccept = {onAccept()})

                        }

                    }
                }
            }
        }
    }
}



@Composable
fun CustomButtons(onAccept: (() -> Unit)?, onDecline: (() -> Unit)?) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
    ) {
        if (onDecline != null) {
            OutlinedButton(
                modifier = Modifier.padding(horizontal = 8.dp).weight(1f),
                onClick = {
                    onDecline()
                },
                shape = RoundedCornerShape(50),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp ),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF006D43), disabledContainerColor = Color(0xFF006D43)),

            ) {

                Icon(
                    painter = painterResource(id = R.drawable.cancel_24dp_fill0_wght200_grad0_opsz24_1),
                    contentDescription = null,
                    tint = Color(0xFF006D43)
                )

                Spacer(modifier = Modifier.width(4.dp))
                Text("Descartar",
                    fontSize = 15.sp,
                    color = Color(0xFF006D43),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                    )
            }
        }

        if (onAccept != null) {
            OutlinedButton(
                modifier = Modifier.padding(horizontal = 8.dp).weight(1f),
                onClick = {
                    onAccept()
                },
                shape = RoundedCornerShape(50),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF006D43), disabledContainerColor = Color(0xFF006D43)),

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.vector),
                    contentDescription = null,
                    tint = Color(0xFF006D43)
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text("Guardar",
                    fontSize = 15.sp,
                    color = Color(0xFF006D43),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}