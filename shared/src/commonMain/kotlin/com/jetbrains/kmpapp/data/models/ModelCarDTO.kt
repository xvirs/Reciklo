package com.jetbrains.kmpapp.data.models

import kotlinx.serialization.Serializable

@Serializable

data class ModelCarDTO(
    val manufacturers: List<Manufacturer>,
    val number: Int,
    val recordsFiltered: Int,
    val recordsTotal: Int,
    val totalPages: Int
)
@Serializable
data class Manufacturer(
    val code: String,
    val description: DescriptionModelCarDTO,
    val id: Int,
    val order: Int
)

@Serializable
data class DescriptionModelCarDTO(
    val description: String,
    val friendlyUrl: String?,
    val highlights: String?,
    val id: Int,
    val keyWords: String?,
    val language: String,
    val metaDescription: String?,
    val name: String,
    val title: String?
)

@Serializable
data class ModelCarListJson(
    val listModelCar : String
)

@Serializable
data class ListManufacturerSerializable(
    val list : List<CategoryToSerializable>
)
