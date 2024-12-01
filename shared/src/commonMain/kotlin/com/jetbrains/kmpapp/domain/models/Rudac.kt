package com.jetbrains.kmpapp.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rudac(
    @SerialName("desarmadero")
    val desarmadero: String? ,
    @SerialName("cuit")
    val cuit: String? ,
    @SerialName("domicilio")
    val domicilio: String? ,
    @SerialName("localidadProvincia")
    val localidadProvincia: String?,
    @SerialName("descripcionDeLaPieza")
    val descripcionDeLaPieza: String? ,
    @SerialName("origenDeLaPieza")
    val origenDeLaPieza: String? ,
    @SerialName("marca")
    val marca: String?,
    @SerialName("estado")
    val estado: String?
)
