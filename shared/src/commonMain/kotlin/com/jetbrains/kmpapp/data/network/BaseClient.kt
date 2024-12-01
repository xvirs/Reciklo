package com.jetbrains.kmpapp.data.network

import com.jetbrains.kmpapp.data.models.BodyType
import com.jetbrains.kmpapp.domain.models.Image
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import kotlinx.serialization.json.Json

class BaseClient() {

    companion object {

        private const val PRO =  "http://reciklo.com.ar:3000/apifront"
        private const val PRE = "http://reciklo.artekium.com:3000/apifront"

        const val BACKEND_URL: String = PRE
        val baseClient: BaseClient = BaseClient()
    }

    private val apiClient: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                json = Json { ignoreUnknownKeys = true }
            )
        }
    }

    internal suspend fun get(
        url: String,
        errorMessage: String,
        token: String,
        query: String = ""
    ): HttpStatus {
        return try {
            val response = apiClient.get(url) {
                contentType(ContentType.Application.Json)
                header(
                    "Authorization",
                    "Bearer $token"
                )
                if (query.isNotEmpty()) {
                    parameter("name", query)
                }
            }
            if (response.status.value in 200..299)
                HttpStatus(httpResponse = response)
            else
                HttpStatus(errorMessage = errorMessage)
        } catch (e: Exception) {
            HttpStatus(errorMessage = "Ups! Atrapaste un error desconocido, vuelve a intentarlo")
        }
    }

    internal suspend fun post(
        url: String,
        errorMessage: String,
        json: String = "",
        token: String,
        bodyType: BodyType,
        parameters: Parameters? = null
        ): HttpStatus {
        return try {

            val response = when(bodyType) {
                BodyType.URLENCODED -> {
                    apiClient.submitForm(
                        url = url,
                        formParameters = parameters!!
                    ) {
                        if (token.isNotEmpty()) {
                            header("Authorization", "Bearer $token")
                        }
                    }
                }
                BodyType.RAW -> {
                    apiClient.post(url) {
                        contentType(ContentType.Application.Json)
                        setBody(json)
                        if (token.isNotEmpty()) {
                            header("Authorization", "Bearer $token")
                        }
                    }
                }
            }


            if (response.status.value in 200..299)
                HttpStatus(httpResponse = response)
            else
                HttpStatus(errorMessage = errorMessage)
        } catch (e: Exception) {
            HttpStatus(errorMessage = "Ups! Atrapaste un error desconocido, vuelve a intentarlo")
        }
    }

    @OptIn(InternalAPI::class)
    internal suspend fun postWhitFile(
        url: String,
        errorMessage: String,
        json: String? = null,
        fileKey: String,
        images: List<Image>,
        token: String
    ): HttpStatus {

        return try {
            val response = apiClient.post(url) {
                contentType(ContentType.MultiPart.FormData)
                setBody(MultiPartFormDataContent(formData {
                    if (!json.isNullOrEmpty()) {
                        append("product", json)
                    }
                    if (images.isNotEmpty()) {
                        images.map { item ->
                            appendInput(fileKey, Headers.build {
                                append(HttpHeaders.ContentType, "image/jpg")
                                append(
                                    HttpHeaders.ContentDisposition,
                                    "filename=\"${"${item.name}.jpg"}\""
                                )
                            }) { buildPacket { writeFully(item.image) } }
                        }
                    }

                }))
                header("Authorization", "Bearer $token")
            }
            if (response.status.value in 200..299) {
                HttpStatus(httpResponse = response)
            } else {
                HttpStatus(errorMessage = errorMessage, errorType = ErrorType.SERVER)
            }
        } catch (e: Exception) {
            HttpStatus(errorMessage = e.message.toString(), errorType = ErrorType.NETWORK)
        }
    }
}

internal data class HttpStatus(
    val httpResponse: HttpResponse? = null,
    val errorMessage: String = "",
    val errorType: ErrorType? = null
)

enum class ErrorType {
    NETWORK,
    SERVER
}