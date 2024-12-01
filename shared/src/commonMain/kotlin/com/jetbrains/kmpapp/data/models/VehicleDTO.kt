package com.jetbrains.kmpapp.data.models

import kotlinx.serialization.Serializable
@Serializable

data class VehicleModelDTO(
    val number: Int,
    val recordsFiltered: Int,
    val recordsTotal: Int,
    val totalPages: Int,
    val vehicleModels: List<VehicleModel>
)
@Serializable

data class VehicleModel(
    val code: String,
    val id: Int,
    val order: Int,
    val description: String
)

@Serializable
data class VehicleVersionDTO(
    val number: Int,
    val recordsFiltered: Int,
    val recordsTotal: Int,
    val totalPages: Int,
    val vehicleVersions: List<VehicleVersionItemDTO>
)
@Serializable
data class VehicleVersionItemDTO(
    val code: String,
    val id: Int,
    val order: Int,
    val description:String
)