package com.jetbrains.kmpapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val token: String
)