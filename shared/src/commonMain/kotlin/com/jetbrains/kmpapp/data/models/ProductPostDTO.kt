package com.jetbrains.kmpapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AttributeDTO(
    val attributeDefault: Boolean,
    val attributeDisplayOnly: Boolean,
    val option: OptionDTO,
    val optionValue: OptionValueDTO,
)

@Serializable
data class OptionDTO(
    val code: String
)

@Serializable
data class OptionValueDTO(
    val id: Int,
    val defaultValue: Boolean,
    val descriptions: List<DescriptionsDTO>
)


@Serializable
data class DescriptionsDTO(
    val name: String,
    val language: String,
)

@Serializable
data class CategoryDTOPost(
    val id: Int
)

@Serializable
data class DescriptionDTO(
    val name: String,
    val title: String,
    val description: String,
    val friendlyUrl: String,
    val highlights: String,
    val keyWords: String,
    val language: String
)

@Serializable
data class ProductSpecificationsDTO(
    val height: Int,
    val length: Int,
    val weight: Int,
    val width: Int,
    val manufacturer: String
)

@Serializable
data class PriceDTO(
    val price: Int,
    val defaultPrice: Boolean
)

@Serializable
data class InventoryDTO(
    val price: PriceDTO,
    val quantity: Int
)

@Serializable
data class ProductPostDTO(
    val attributes: List<AttributeDTO>,
    val categories: List<CategoryDTOPost>,
    val visible: Boolean,
    val sortOrder: Int,
    val dateAvailable: String,
    val descriptions: List<DescriptionDTO>,
    val productSpecifications: ProductSpecificationsDTO,
    val inventory: InventoryDTO,
    val type: String
)
