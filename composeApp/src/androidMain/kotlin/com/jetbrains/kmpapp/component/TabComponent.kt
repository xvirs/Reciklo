package com.jetbrains.kmpapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.colors.greenIcon

@Composable
fun TabComponent(
    primaryTabText: String,
    secondaryTabText: String,
    primaryIcon: Painter,
    secondaryIcon: Painter,
    selectedTab: Int? = null,
    onPrimaryTabSelected: @Composable () -> Unit,
    onSecondaryTabSelected: @Composable () -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(selectedTab ?: 0) }
    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->
                val modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                Box(
                    modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(greenIcon),
                )
            }
        ) {
            Tab(
                text = primaryTabText,
                icon = primaryIcon
            ) {
                selectedTabIndex = 0
            }
            Tab(
                text = secondaryTabText,
                icon = secondaryIcon
            ) {
                selectedTabIndex = 1

            }
        }
        when (selectedTabIndex) {
            0 -> {
                onPrimaryTabSelected()
            }
            1 -> {
                onSecondaryTabSelected()
            }
        }
    }
}

@Composable
fun Tab(
    text: String,
    icon: Painter,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.Black)
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color.Black)
    }

}