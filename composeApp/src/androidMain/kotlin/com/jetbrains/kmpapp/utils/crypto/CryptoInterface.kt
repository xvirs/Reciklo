package com.jetbrains.kmpapp.utils.crypto

import java.io.InputStream
import java.io.OutputStream

interface CryptoInterface {
    fun encrypt(
        alias: String,
        bytes: ByteArray,
        outputStream: OutputStream
    ): ByteArray?
    fun decrypt(
        alias: String,
        inputStream: InputStream
    ): ByteArray?

}