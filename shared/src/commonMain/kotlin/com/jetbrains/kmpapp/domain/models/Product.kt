package com.jetbrains.kmpapp.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(

    val id: Long? = null,
    var image: List<String> = emptyList(),
    var productName: String? = "null",
    var location: String? = "null",
    var description: String? = "null",
    var numberRUDAC: String? = "null",
    var listRudac : List<String> = emptyList(),
    var marca: String? = "null",
    var tittleMarca:String? =  "null",
    var model: String? = "null",
    var version : String? = "null",
    var type: String? = "null",
    var category: Int? = 0,
    var chassis: String? = "null",
    var year: String? = "0",
    var quantity: String? = "0",
    var price: String? = "0",
    var height: String? = "0",
    var width: String? = "0",
    var length: String? = "0",
    var weight: String? = "0",
    var productsFailure: List<ProductFailure> = emptyList(),
    var titlesFailures : List<String> = emptyList(),
    var state: StateProduct? = null,
)

@Serializable
data class ProductResponse(
    val id: Int
)

@Serializable
data class ProductFailure(
    val failure: String,
    val uri: String
)

enum class StateProduct {
    SYNCED,
    FAILED_SERVER,
    FAILED_NETWORK,
}
