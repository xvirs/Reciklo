package com.jetbrains.kmpapp.model

data class BottomBarItemModel(
    val title: String,
    val resource: Int,
    val type : BottomBarItemType
)

enum class BottomBarItemType{
    HOME,
    ADD,
    STOCK
}

