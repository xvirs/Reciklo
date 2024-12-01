package com.jetbrains.kmpapp.domain.models

sealed class SessionManager<out T>{

    data class Offline(val message : String) : SessionManager<Nothing>()

    data class Online <out T>(val value : T) : SessionManager <T>()

}
