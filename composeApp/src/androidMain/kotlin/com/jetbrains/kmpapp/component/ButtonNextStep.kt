package com.jetbrains.kmpapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetbrains.kmpapp.colors.white


@Composable
fun ButtonNextStep(
    nextStep: () -> Unit,
    text: String,
    enabled: State<Boolean> = mutableStateOf(false)
) {
    var status by remember { mutableStateOf(true) }
    if (enabled.value)
        status = false

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedButton(
            modifier = Modifier
                .height(50.dp),
            onClick = {
                nextStep()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006D43)),
            enabled = status
        ) {
            Text(
                text = text,
                color = white,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400
            )
        }
    }

}