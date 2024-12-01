package com.jetbrains.kmpapp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.R

@Composable
fun EmptySpace() {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.ic_empty_space),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text("Todavía no hay productos cargados")
        Spacer(modifier = Modifier.size(5.dp))
        Text("¡Iniciá tu primer carga!")
    }
}