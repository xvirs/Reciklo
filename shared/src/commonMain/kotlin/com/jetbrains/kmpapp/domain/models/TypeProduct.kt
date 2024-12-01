package com.jetbrains.kmpapp.domain.models


data class TypeProduct (
    val listType : List<ListTypeProduct>
)

data class ListTypeProduct(
    val allowAddToCart: Boolean,
    val code: String,
    val description: Description,
    val dimensions: Dimensions?,
    val id: Int,
    val visible: Boolean
)


data class Dimensions(
    val length: Int?,
    val width: Int?,
    val height: Int?,
    val weight: Int?
)

