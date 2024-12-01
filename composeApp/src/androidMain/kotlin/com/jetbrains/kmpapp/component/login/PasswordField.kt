package com.jetbrains.kmpapp.component.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.jetbrains.kmpapp.colors.greenIcon

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    isError: Boolean,
    errorMessage: String
) {
    var visibility by remember { mutableStateOf(isPasswordVisible) }
    val inputVisible by rememberUpdatedState(newValue = isError)
    val errorMessageUpdate by rememberUpdatedState(newValue = errorMessage)

    OutlinedTextField(
        modifier = Modifier.fillMaxSize(),
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        visualTransformation = if (visibility) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = { visibility = !visibility }) {
                val visibilityIcon =
                    if (visibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (visibility) "Show password" else "Hide password"
                Icon(imageVector = visibilityIcon, contentDescription = description)
            }
        },
        label = { Text(text = "Password") },
        isError = inputVisible,
        supportingText = {
            if (inputVisible)
                Text(errorMessageUpdate)
        },
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = greenIcon, focusedLabelColor = greenIcon)
    )
}