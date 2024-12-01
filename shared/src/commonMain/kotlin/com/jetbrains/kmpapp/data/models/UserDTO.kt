package com.jetbrains.kmpapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String
)

@Serializable
data class UserSerializable(
    val user : String
)
