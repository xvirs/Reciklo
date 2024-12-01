package com.jetbrains.kmpapp.domain.models

data class Description(
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

data class ListCategory(
    val description: List<Description>
)
