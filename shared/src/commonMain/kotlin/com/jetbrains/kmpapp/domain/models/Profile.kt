package com.jetbrains.kmpapp.domain.models

data class Profile(
    val id: Int,
    val active: Boolean,
    val firstName: String,
    val lastName: String,
)
