package com.jetbrains.kmpapp.component.appBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jetbrains.kmpapp.R
import com.jetbrains.kmpapp.colors.appsBarColor

@Composable
fun TopAppBar( titleTopBar:String, isLogout:Boolean, navigation:()->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth().height(56.dp)
            .background(appsBarColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        when(isLogout){
            true -> {
                IconButton(onClick = { navigation()}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                        contentDescription = null
                    )
                }
            }
            false -> {
                IconButton(onClick = { navigation()}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        }
        if (titleTopBar != "Home"){
            Text(
                text = titleTopBar,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            AsyncImage(
                model = R.drawable.reciklo_logo, contentDescription = "logo app",
                modifier = Modifier
                    .height(230.dp)
                    .width(230.dp)
            )
        }

        Spacer(modifier = Modifier.width(48.dp))
    }
}
