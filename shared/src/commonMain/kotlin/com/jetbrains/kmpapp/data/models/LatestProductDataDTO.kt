package com.jetbrains.kmpapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LatestProductDataDTO(
    val number: Int,
    val products: List<LatestProductDataDTOItem>,
    val recordsFiltered: Int,
    val recordsTotal: Int,
    val totalPages: Int
)
@Serializable
data class LatestProductDataDTOItem(
    val id:Int,
    val name:String?,
    val imageUrl: String?,
    val manufacturer: String,
    val price: String,
    val rudac: String?
)