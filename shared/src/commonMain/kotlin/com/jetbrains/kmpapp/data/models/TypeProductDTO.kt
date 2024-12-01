package com.jetbrains.kmpapp.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TypeProductDTO(
    val list: List<ListTypeDTO>,
    val number: Int,
    val recordsFiltered: Int,
    val recordsTotal: Int,
    val totalPages: Int
)

@Serializable
data class ListTypeDTO (
    val allowAddToCart: Boolean,
    val code: String,
    val description: Description,
    val dimensions: Dimensions?,
    val id: Int,
    val visible: Boolean
)

@Serializable
data class JsonListTypeDTO(
    val listTypes : String
)

@Serializable
data class TypeProductToSerializable (
    @SerialName("listType") val listType : List<ListTypeProductToSerializable>
)


@Serializable
data class ListTypeProductToSerializable(
    val allowAddToCart: Boolean,
    val code: String,
    val description: CategoryToSerializable,
    val dimensions: DimensionsToSerializable?,
    val id: Int,
    val visible: Boolean
)

@Serializable
data class CategoryToSerializable(
    val code : String?,
    val friendlyUrl: String?,
    val highlights: String?,
    val id: Int?,
    val keyWords: String?,
    val language: String?,
    val metaDescription: String?,
    val name: String?,
    val title: String?
)

@Serializable
data class DimensionsToSerializable(
    val height: Int?,
    val length: Int?,
    val width: Int?,
    val weight: Int?
)

@Serializable
data class Dimensions(
    val height: Int?,
    val length: Int?,
    val width: Int?,
    val weight: Int?
)
