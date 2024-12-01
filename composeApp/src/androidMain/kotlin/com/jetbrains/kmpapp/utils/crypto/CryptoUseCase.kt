package com.jetbrains.kmpapp.utils.crypto

import android.content.Context
import android.util.Base64
import com.jetbrains.kmpapp.domain.models.StatusResult
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class CryptoUseCase(private val crypto: CryptoInterface) {

    /**
     * Este metodo se encargara de encriptar el archivo con su clave especifica.
     *
     * El [alias] se trata del nombre de la clave encriptada.
     *
     * El [fileName] es el nombre del archivo encriptado.
     *
     * Los datos mencionados se alojan en la enum class [AliasTag]
     */

    fun encrypt(
        alias: String,
        file: File,
        data: String,
    ): StatusResult<String> {
        val bytes = data.encodeToByteArray()
        return try {
            FileOutputStream(file).use { fos ->
                when (val response = crypto.encrypt(bytes = bytes, outputStream = fos, alias = alias)) {
                    null -> StatusResult.Error("Error al encriptar")
                    else -> StatusResult.Success(Base64.encodeToString(response, Base64.DEFAULT))
                }
            }
        } catch (e: Exception) {
            StatusResult.Error("Error al encriptar: ${e.message}")
        }
    }

    /**
     * Este metodo se encargara de desencriptar el archivo con su clave especifica.
     *
     * El [alias] se trata del nombre de la clave encriptada.
     *
     * El [fileName] es el nombre del archivo encriptado.
     *
     * Los datos mencionados se alojan en la enum class [AliasTag]
     */
    fun decrypt(alias: String, file: File): StatusResult<String> {
        return if (file.exists()) {
            try {
                FileInputStream(file).use { fis ->
                    when (val response = crypto.decrypt(alias = alias, inputStream = fis)) {
                        null -> StatusResult.Error("Error al desencriptar")
                        else -> StatusResult.Success(String(response))
                    }
                }
            } catch (e: Exception) {
                StatusResult.Error("Error al desencriptar: ${e.message}")
            }
        } else {
            StatusResult.Error("El archivo no existe")
        }
    }
}