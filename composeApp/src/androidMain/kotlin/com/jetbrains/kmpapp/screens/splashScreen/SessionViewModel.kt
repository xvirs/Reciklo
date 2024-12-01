package com.jetbrains.kmpapp.screens.splashScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.usecase.RefreshSessionUseCase
import com.jetbrains.kmpapp.utils.crypto.AliasTag
import com.jetbrains.kmpapp.utils.crypto.CryptoUseCase
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class SessionViewModel(
    private val cryptoUseCase: CryptoUseCase,
    private val refreshSessionUseCase: RefreshSessionUseCase
):ViewModel() {
    private val _statusSession = MutableStateFlow<StatusResult<String>>(StatusResult.Error("no inicializado"))
        val statusSession = _statusSession.asStateFlow()

    private val _eventNavigation = MutableStateFlow(false)
        val eventNavigation = _eventNavigation.asStateFlow()

    fun navigationReady() {
        _eventNavigation.value = false
    }

    suspend fun checkSession(file: File){
        val defDecrypt = viewModelScope.async(Dispatchers.Default, CoroutineStart.LAZY) {
            when(val response = cryptoUseCase.decrypt(AliasTag.TOKEN.alias, file)){
                is StatusResult.Error -> {
                    Log.e("refresh", "fallo")
                    StatusResult.Error(response.message)
                }
                is StatusResult.Success -> {
                    Log.e("refresh", "refresh viejo")
                    StatusResult.Success(response.value)
                }
            }
        }
        val defRefresh = viewModelScope.async(Dispatchers.Default, CoroutineStart.LAZY) {
            when(defDecrypt.await()){
                is StatusResult.Error -> {
                    Log.e("refresh", "fallo")
                     StatusResult.Error("Fallo el desencriptado")
                }
                is StatusResult.Success -> {
                    when(val result = refreshSessionUseCase.invoke((defDecrypt.await() as StatusResult.Success<String>).value)){
                        is StatusResult.Error -> {
                             StatusResult.Error("token de refresh vencido")
                        }
                        is StatusResult.Success -> {
                             StatusResult.Success(result.value.token)
                        }
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            when(defRefresh.await()){
                is StatusResult.Error -> {
                    _statusSession.value = StatusResult.Error((defRefresh.await() as StatusResult.Error).message)
                    _eventNavigation.value = true
                }
                is StatusResult.Success -> {
                    _statusSession.value = StatusResult.Success((defRefresh.await() as StatusResult.Success<String>).value)
                    _eventNavigation.value = false
                    when(cryptoUseCase.encrypt(AliasTag.TOKEN.alias, file,(defRefresh.await() as StatusResult.Success<String>).value)){
                        is StatusResult.Error ->Log.e("refresh", "refresh no guardado")
                        is StatusResult.Success -> Log.e("refresh", "refresh guardado")
                    }
                }
            }
        }

    }

}