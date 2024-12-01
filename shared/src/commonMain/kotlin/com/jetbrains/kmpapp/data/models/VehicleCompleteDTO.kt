package com.jetbrains.kmpapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserVehicleCompleteDTO(
    val access_token: String,
    val token_type: String
)

@Serializable
data class DataVehicleCompleteDTO(
    val Marca: String,
    val Modelo: String,
    @SerialName("N° de Chasis") val NumeroDeChasis: String,
    val RESULT: String,
    val ANIO: String
)

@Serializable
data class ListRudacVehicleCompleteDTO(
    val  NumericData: List<String>
)