package com.jetbrains.kmpapp.component.addProductDataScreen

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import com.jetbrains.kmpapp.colors.greenIcon
import com.jetbrains.kmpapp.colors.greenOnline


@Composable
fun RenderTextField(
    value: String,
    textChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    isEnabled: Boolean = true,
    modifier : Modifier,
    isError: Boolean  = false,
    trailingIcon : ImageVector? = null
) {


    TextField(
        value = value,
        onValueChange = { newValue ->
            textChange(newValue)
        },
        label = { Text(
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )  },
        colors = TextFieldDefaults.colors(
            cursorColor = if (isError) Color.Red else Color.Black,
            focusedContainerColor = greenOnline,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color(0xFFFBFBFB),
            disabledTextColor = Color(0xFF171D19),
            focusedLabelColor = greenIcon,
            focusedIndicatorColor = greenIcon,
            errorContainerColor = Color.Transparent
        ),
        trailingIcon = {
            if (isError) {
                Icon(Icons.Filled.Error, contentDescription =null, tint = Color.Red)
            } else if (trailingIcon!=null) {
                Icon(Icons.Outlined.KeyboardArrowDown, contentDescription =null, tint = greenIcon)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType
        ),
        modifier = modifier,
        maxLines = 1,
        enabled = isEnabled,
        isError = isError,
    )
}
