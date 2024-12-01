package com.jetbrains.kmpapp.utils.crypto
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

open class CryptoManager() : CryptoInterface {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private fun getEncryptCipher(alias: String): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey(alias))
        }
    }

    private fun getDecryptCipherForIv(alias: String, iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(alias), GCMParameterSpec(128, iv))
        }
    }

    private fun getKey(alias: String): SecretKey {
        val existingKey = keyStore.getEntry(alias, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey(alias)
    }

    private fun createKey(alias: String): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    override fun encrypt(alias: String, bytes: ByteArray, outputStream: OutputStream): ByteArray? {
        return try {
            val cipher = getEncryptCipher(alias)
            val encryptedBytes = cipher.doFinal(bytes)
            outputStream.use {
                it.write(cipher.iv.size)
                it.write(cipher.iv)
                it.write(encryptedBytes.size)
                it.write(encryptedBytes)
            }
            encryptedBytes
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun decrypt(alias: String, inputStream: InputStream): ByteArray? {
        return try {
            inputStream.use {
                val ivSize = it.read()
                val iv = ByteArray(ivSize)
                it.read(iv)

                val encryptedBytesSize = it.read()
                val encryptedBytes = ByteArray(encryptedBytesSize)
                it.read(encryptedBytes)

                getDecryptCipherForIv(alias, iv).doFinal(encryptedBytes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }
}
