package com.jetbrains.kmpapp.domain.models



data class UserVehicleComplete(
    val access_token: String,
    val token_type: String
)

data class DataVehicleComplete(
    val Marca: String,
    val Modelo: String,
    val NumeroDeChasis: String,
    val RESULT: String,
    val ANIO: String
)

data class ListRudacVehicleComplete(
    val  data: List<String>
)