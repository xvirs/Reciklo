package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManagerUseCase() {

    private var _session: MutableStateFlow<SessionManager<User>> = MutableStateFlow(SessionManager.Offline("Modo offline base"))
    val session = _session.asStateFlow()
    fun getStatus() : SessionManager<User>{
        return when(session.value){
            is SessionManager.Offline -> SessionManager.Offline("OFF")
            is SessionManager.Online -> SessionManager.Online((session.value as SessionManager.Online<User>).value)
        }
    }

    fun getToken():String ? = when(val response = getStatus()){
        is SessionManager.Offline -> null
        is SessionManager.Online -> response.value.token
    }


    fun updateStatus(sessionManager: SessionManager<User>){
        _session.value = sessionManager
    }
}