package com.jetbrains.kmpapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val categories: List<Status>,
    val number: Int,
    val recordsFiltered: Int,
    val recordsTotal: Int,
    val totalPages: Int
)

@Serializable
data class Status(
    val children: List<Child>,
    val code: String,
    val depth: Int,
    val description: Description,
    val featured: Boolean,
    val id: Int,
    val lineage: String,
    val parent: Parent?,
    val productCount: Int,
    val sortOrder: Int,
    val store: String,
    val visible: Boolean
)

@Serializable
data class Description(
    val description: String?,
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
data class Parent(
    val code: String,
    val description: Description,
    val id: Int
)

@Serializable
data class Child(
    val placeholder: Unit = Unit // Parámetro sin propiedades
)

@Serializable
data class JsonListCategoryDTO(
    val listCategory : String
)

@Serializable
data class ListCategorySerializable(
    @SerialName("description")val description: List<CategoryToSerializable>
)
