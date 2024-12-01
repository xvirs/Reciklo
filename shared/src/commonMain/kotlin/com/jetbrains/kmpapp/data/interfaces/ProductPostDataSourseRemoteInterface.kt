package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.Image
import com.jetbrains.kmpapp.domain.models.ProductResponse
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.data.models.ProductPostDTO

interface ProductPostDataSourceRemoteInterface {
    suspend fun createProduct(productPostDTO: ProductPostDTO, token : String, listImages: List<Image>): StatusResult<ProductResponse>
}