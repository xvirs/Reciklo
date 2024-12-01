package com.jetbrains.kmpapp.domain.models

data class LatestProductData(
    val productToRenders : List<ProductToRender>
)

data class ProductToRender(
    val id:Int? = null ,
    val name:String,
    val imageUrl: String,
    val price : String,
    val manufacturer : String,
    val rudac: String?
)