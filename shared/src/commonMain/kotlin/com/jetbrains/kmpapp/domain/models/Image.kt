package com.jetbrains.kmpapp.domain.models

data class Image (
    val image : ByteArray = ByteArray(0),
    val name : String = ""
)