package com.jetbrains.kmpapp.domain.models

sealed class StatusResult <out T> {

    data class Success<out T>(val value: T) : StatusResult<T>()

    data class Error(val message: String, val errorType: ErrorType? = null) : StatusResult<Nothing>()

    enum class ErrorType {
        NETWORK,
        SERVER,
        UNKNOWN
    }

}