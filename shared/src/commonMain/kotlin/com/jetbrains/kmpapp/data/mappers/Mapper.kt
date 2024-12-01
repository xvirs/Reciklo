package com.jetbrains.kmpapp.data.mappers

import com.jetbrains.kmpapp.domain.models.Description
import com.jetbrains.kmpapp.domain.models.ListCategory
import com.jetbrains.kmpapp.domain.models.ListManufacturer
import com.jetbrains.kmpapp.domain.models.ListTypeProduct
import com.jetbrains.kmpapp.domain.models.Product
import com.jetbrains.kmpapp.domain.models.TypeProduct
import com.jetbrains.kmpapp.domain.models.VehicleModel
import com.jetbrains.kmpapp.domain.models.VehicleVersion
import com.jetbrains.kmpapp.data.models.AttributeDTO
import com.jetbrains.kmpapp.data.models.CategoryDTOPost
import com.jetbrains.kmpapp.data.models.CategoryDto
import com.jetbrains.kmpapp.data.models.CategoryToSerializable
import com.jetbrains.kmpapp.data.models.DataVehicleCompleteDTO
import com.jetbrains.kmpapp.data.models.DescriptionDTO
import com.jetbrains.kmpapp.data.models.DescriptionsDTO
import com.jetbrains.kmpapp.data.models.DimensionsToSerializable
import com.jetbrains.kmpapp.data.models.InventoryDTO
import com.jetbrains.kmpapp.data.models.LatestProductDataDTO
import com.jetbrains.kmpapp.data.models.ListCategorySerializable
import com.jetbrains.kmpapp.data.models.ListManufacturerSerializable
import com.jetbrains.kmpapp.data.models.ListRudacVehicleCompleteDTO
import com.jetbrains.kmpapp.data.models.ListTypeProductToSerializable
import com.jetbrains.kmpapp.data.models.ModelCarDTO
import com.jetbrains.kmpapp.data.models.OptionDTO
import com.jetbrains.kmpapp.data.models.OptionValueDTO
import com.jetbrains.kmpapp.data.models.PriceDTO
import com.jetbrains.kmpapp.data.models.ProductPostDTO
import com.jetbrains.kmpapp.data.models.ProductSpecificationsDTO
import com.jetbrains.kmpapp.data.models.ProfileDTO
import com.jetbrains.kmpapp.data.models.TypeProductDTO
import com.jetbrains.kmpapp.data.models.TypeProductToSerializable
import com.jetbrains.kmpapp.data.models.UserVehicleCompleteDTO
import com.jetbrains.kmpapp.data.models.VehicleModelDTO
import com.jetbrains.kmpapp.data.models.VehicleVersionDTO
import com.jetbrains.kmpapp.domain.models.DataVehicleComplete
import com.jetbrains.kmpapp.domain.models.Dimensions
import com.jetbrains.kmpapp.domain.models.ProductToRender
import com.jetbrains.kmpapp.domain.models.LatestProductData
import com.jetbrains.kmpapp.domain.models.ListRudacVehicleComplete
import com.jetbrains.kmpapp.domain.models.Profile
import com.jetbrains.kmpapp.domain.models.UserVehicleComplete

fun CategoryDto.toModel(): ListCategory {

    val list: List<Description> = this.categories.map {
        Description(
            code = it.code,
            friendlyUrl = it.description.friendlyUrl,
            highlights = it.description.highlights,
            id = it.id,
            keyWords = it.description.keyWords,
            language = it.description.language,
            metaDescription = it.description.metaDescription,
            name = it.description.name,
            title = it.description.title
        )
    }

    return ListCategory(
        description = list
    )
}

fun CategoryDto.toModelSerializable(): ListCategorySerializable {

    val list: List<CategoryToSerializable> = this.categories.map {
        CategoryToSerializable(
            code = it.code,
            friendlyUrl = it.description.friendlyUrl,
            highlights = it.description.highlights,
            id = it.id,
            keyWords = it.description.keyWords,
            language = it.description.language,
            metaDescription = it.description.metaDescription,
            name = it.description.name,
            title = it.description.title
        )
    }

    return ListCategorySerializable(
        description = list
    )
}

fun TypeProductDTO.toModel(): TypeProduct {

    val list: List<ListTypeProduct> = this.list.map {
        ListTypeProduct(
            allowAddToCart = it.allowAddToCart,
            code = it.code,
            description = Description(
                code = it.code,
                friendlyUrl = it.description.friendlyUrl,
                highlights = it.description.highlights,
                id = it.description.id,
                keyWords = it.description.keyWords,
                language = it.description.language,
                metaDescription = it.description.metaDescription,
                name = it.description.name,
                title = it.description.title
            ),
            dimensions = Dimensions(
                height = it.dimensions?.height,
                length = it.dimensions?.length,
                weight = it.dimensions?.weight,
                width = it.dimensions?.width
            ),
            id = it.id,
            visible = it.visible
        )
    }

    return TypeProduct(
        listType = list
    )
}


fun TypeProductDTO.toModelSerializable(): TypeProductToSerializable {
    val list: List<ListTypeProductToSerializable> = this.list.map {
        ListTypeProductToSerializable(
            allowAddToCart = it.allowAddToCart,
            code = it.code,
            description = CategoryToSerializable(
                code = it.code,
                friendlyUrl = it.description.friendlyUrl,
                highlights = it.description.highlights,
                id = it.description.id,
                keyWords = it.description.keyWords,
                language = it.description.language,
                metaDescription = it.description.metaDescription,
                name = it.description.name,
                title = it.description.title
            ),
            dimensions = DimensionsToSerializable(
                height = it.dimensions?.height,
                length = it.dimensions?.length,
                weight = it.dimensions?.weight,
                width = it.dimensions?.width
            ),
            id = it.id,
            visible = it.visible
        )
    }

    return TypeProductToSerializable(
        listType = list
    )
}

fun TypeProductToSerializable.toTypeProduct(): TypeProduct {
    val list: List<ListTypeProduct> = this.listType.map {
        ListTypeProduct(
            allowAddToCart = it.allowAddToCart,
            code = it.code,
            description = Description(
                code = it.code,
                friendlyUrl = it.description.friendlyUrl,
                highlights = it.description.highlights,
                id = it.description.id,
                keyWords = it.description.keyWords,
                language = it.description.language,
                metaDescription = it.description.metaDescription,
                name = it.description.name,
                title = it.description.title
            ),
            dimensions = Dimensions(
                height = it.dimensions?.height,
                length = it.dimensions?.length,
                weight = it.dimensions?.weight,
                width = it.dimensions?.width
            ),
            id = it.id,
            visible = it.visible
        )
    }

    return TypeProduct(
        listType = list
    )
}

fun ListCategorySerializable.toModel(): ListCategory {
    val list: List<Description> = this.description.map {
        Description(
            code = it.code,
            friendlyUrl = it.friendlyUrl,
            highlights = it.highlights,
            id = it.id,
            keyWords = it.keyWords,
            language = it.language,
            metaDescription = it.metaDescription,
            name = it.name,
            title = it.title
        )
    }

    return ListCategory(
        description = list
    )
}


fun mapProductToDTO(product: Product): ProductPostDTO {

    val attributeModel: AttributeDTO? = if (product.model.isNullOrEmpty()) null else AttributeDTO(
        attributeDefault = true,
        attributeDisplayOnly = true,
        option = OptionDTO(code = "MODELO"),
        optionValue = OptionValueDTO(
            id = 0,
            defaultValue = true,
            descriptions = listOf(DescriptionsDTO(name = product.model!!, language = "es"))
        )
    )

    val attributeVersion: AttributeDTO? = if (product.version.isNullOrEmpty()) null else AttributeDTO(
        attributeDefault = true,
        attributeDisplayOnly = true,
        option = OptionDTO(code = "VERSION"),
        optionValue = OptionValueDTO(
            id = 0,
            defaultValue = true,
            descriptions = listOf(DescriptionsDTO(name = product.version!!, language = "es"))
        )
    )

    val attributeYear: AttributeDTO? = if (product.year.isNullOrEmpty()) null else AttributeDTO(
        attributeDefault = true,
        attributeDisplayOnly = true,
        option = OptionDTO(code = "ANIO"),
        optionValue = OptionValueDTO(
            id = 0,
            defaultValue = true,
            descriptions = listOf(DescriptionsDTO(name = product.year!!, language = "es"))
        )
    )

    val attributeLocation: AttributeDTO? = if (product.location.isNullOrEmpty()) null else AttributeDTO(
        attributeDefault = true,
        attributeDisplayOnly = true,
        option = OptionDTO(code = "LOCACION"),
        optionValue = OptionValueDTO(
            id = 0,
            defaultValue = true,
            descriptions = listOf(DescriptionsDTO(name = product.location!!, language = "es"))
        )
    )

    // Generar atributos RUDAC desde listRudac
    val attributeRudacList: List<AttributeDTO> = product.listRudac.map { rudac ->
        AttributeDTO(
            attributeDefault = true,
            attributeDisplayOnly = true,
            option = OptionDTO(code = "RUDAC"),
            optionValue = OptionValueDTO(
                id = 0,
                defaultValue = true,
                descriptions = listOf(DescriptionsDTO(name = rudac, language = "es"))
            )
        )
    }

    // Combinar todos los atributos
    val attributes = listOfNotNull(
        attributeModel,
        attributeVersion,
        attributeYear,
        attributeLocation
    ) + attributeRudacList

    return ProductPostDTO(
        attributes = attributes,
        categories = listOf(CategoryDTOPost(id = product.category ?: 0)),
        visible = true,
        sortOrder = 0,
        dateAvailable = "",
        descriptions = listOf(
            DescriptionDTO(
                name = product.productName ?: "",
                title = "",
                description = descriptionCreate(product),
                friendlyUrl = "",
                highlights = "",
                keyWords = product.tittleMarca!! + " " + product.model + " " + product.version,
                language = "es"
            )
        ),
        productSpecifications = ProductSpecificationsDTO(
            height = product.height?.toIntOrNull() ?: 0,
            length = product.length?.toIntOrNull() ?: 0,
            weight = product.weight?.toIntOrNull() ?: 0,
            width = product.width?.toIntOrNull() ?: 0,
            manufacturer = product.marca ?: ""
        ),
        inventory = InventoryDTO(
            price = PriceDTO(
                price = product.price?.replace(Regex("\\D"), "")?.toIntOrNull() ?: 0,
                defaultPrice = true
            ),
            quantity = product.quantity?.toIntOrNull() ?: 0
        ),
        type = product.type ?: ""
    )
}



private fun descriptionCreate(product: Product): String {
    return "Chassis: ${product.chassis}.\n " +
            "Marca: ${product.tittleMarca}.\n " +
            "Modelo: ${product.model}.\n " +
            "Version: ${product.version}.\n " +
            "Numero de RUDAC: ${product.numberRUDAC}.\n " +
            "Año: ${product.year}.\n " +
            "Demeritos: ${
                product.titlesFailures.joinToString(separator = ".\n ") {
                    " - ${it}"
                }
            }\n" +
            "Descripcion: ${product.description}"
}

fun ModelCarDTO.toModel(): ListManufacturer {
    val list: List<Description> = this.manufacturers.map {
        Description(
            friendlyUrl = it.description.friendlyUrl,
            highlights = it.description.highlights,
            code = it.code,
            id = it.id,
            keyWords = it.description.keyWords,
            language = it.description.language,
            metaDescription = it.description.metaDescription,
            name = it.description.name,
            title = it.description.title,
        )
    }

    return ListManufacturer(list = list)
}

fun ModelCarDTO.toModelSerializable(): ListManufacturerSerializable {

    val list: List<CategoryToSerializable> = this.manufacturers.map {
        CategoryToSerializable(
            friendlyUrl = it.description.friendlyUrl,
            highlights = it.description.highlights,
            code = it.code,
            id = it.description.id,
            keyWords = it.description.keyWords,
            language = it.description.language,
            metaDescription = it.description.metaDescription,
            name = it.description.name,
            title = it.description.title,
        )
    }

    return ListManufacturerSerializable(
        list = list
    )

}

fun ListManufacturerSerializable.toModel(): ListManufacturer {
    val list: List<Description> = this.list.map {
        Description(
            friendlyUrl = it.friendlyUrl,
            highlights = it.highlights,
            code = it.code,
            id = it.id,
            keyWords = it.keyWords,
            language = it.language,
            metaDescription = it.metaDescription,
            name = it.name,
            title = it.title,
        )
    }

    return ListManufacturer(list = list)
}

fun VehicleModelDTO.toModel(): VehicleModel {
    val list = this.vehicleModels.map {
        Description(
            friendlyUrl = "",
            highlights = "",
            code = it.code,
            id = it.id,
            keyWords = "",
            language = "",
            metaDescription = "",
            name = it.description,
            title = it.description,
        )
    }

    return VehicleModel(list = list)
}

fun VehicleVersionDTO.toModel(): VehicleVersion {
    val list = this.vehicleVersions.map {
        Description(
            code = it.code,
            friendlyUrl = null,
            highlights = null,
            id = it.id,
            keyWords = null,
            language = null,
            metaDescription = null,
            name = it.description,
            title = it.description
        )
    }

    return VehicleVersion(list = list)
}

//latest saved

fun LatestProductDataDTO.toModel() : LatestProductData{
    val list = this.products.map {
        ProductToRender(
            id = it.id,
            name=it.name?:"",
            imageUrl = it.imageUrl ?: "",
            price = it.price,
            manufacturer = it.manufacturer,
            rudac = it.rudac
        )
    }
    return LatestProductData(list)
}

//Profile

fun ProfileDTO.toModel():Profile{
    return Profile(
        id = this.id,
        active = this.active,
        firstName = this.firstName ?: "",
        lastName = this.lastName ?: ""
    )
}

fun UserVehicleCompleteDTO.toModel(): UserVehicleComplete {
    return UserVehicleComplete(this.access_token, this.token_type)
}

fun DataVehicleCompleteDTO.toModel(): DataVehicleComplete {
    return DataVehicleComplete(this.Marca , this.Modelo, this.NumeroDeChasis, this.RESULT, this.ANIO)
}

fun ListRudacVehicleCompleteDTO.toModel(): ListRudacVehicleComplete{
    val list = this.NumericData.map {it}
    return  ListRudacVehicleComplete(list)
}