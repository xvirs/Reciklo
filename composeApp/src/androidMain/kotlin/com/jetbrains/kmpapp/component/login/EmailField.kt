package com.jetbrains.kmpapp.component.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import com.jetbrains.kmpapp.colors.greenIcon

@Composable
fun EmailField(
    value: String,
    textChange: (String) -> Unit,
    isError: Boolean,
    errorMessage: String
) {

    val updateStatus by rememberUpdatedState(newValue = isError)
    val errorMessageUpdate by rememberUpdatedState(newValue = errorMessage)

    OutlinedTextField(
        value = value,
        onValueChange = { textChange(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Email", overflow = TextOverflow.Ellipsis) },        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        isError = updateStatus,
        supportingText = { if (updateStatus) Text(text = errorMessageUpdate) },
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = greenIcon, focusedLabelColor = greenIcon)
    )
}
