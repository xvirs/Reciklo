package com.jetbrains.kmpapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jetbrains.kmpapp.colors.gray
import com.jetbrains.kmpapp.domain.models.Description
import com.jetbrains.kmpapp.utils.TypeRenderDropDown

@Composable
fun RenderDropDown(
    typeRenderDropDown: TypeRenderDropDown,
    titleDropDown: String,
    listData: List<Description>,
    saveInfo: (TypeRenderDropDown, Description) -> Unit,
    isActive : Boolean,
) {
    val updateTitleDropDown by rememberUpdatedState(newValue = titleDropDown)
    var expanded by remember {
        mutableStateOf(false)
    }
    TextButton(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            expanded = true
        },
        enabled = isActive,
        colors = ButtonDefaults.textButtonColors(
            contentColor = gray,
            disabledContentColor = Color.Gray
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = updateTitleDropDown,
                style = TextStyle(
                    fontWeight = FontWeight.Normal, // Cambia esto a FontWeight.Normal para no negrita
                    fontSize = 16.sp // Cambia esto a 16.sp para el tamaño de la letra
                )
            )

            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Localized description")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
        ) {
            listData.onEach {
                DropdownMenuItem(
                    text = { (it.title ?: it.name)?.let { value -> Text(text = value) } },
                    onClick = {
                        expanded = false
                        saveInfo(typeRenderDropDown, it)
                    })
            }
        }
    }
}