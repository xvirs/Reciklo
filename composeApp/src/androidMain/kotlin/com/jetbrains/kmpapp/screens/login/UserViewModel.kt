package com.jetbrains.kmpapp.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.data.models.UserDTO
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User
import com.jetbrains.kmpapp.domain.usecase.LoginUseCase
import com.jetbrains.kmpapp.domain.usecase.RefreshSessionUseCase
import com.jetbrains.kmpapp.domain.usecase.SessionLocalUseCase
import com.jetbrains.kmpapp.utils.crypto.AliasTag
import com.jetbrains.kmpapp.utils.crypto.CryptoUseCase
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class UserViewModel(
    private val loginUseCase: LoginUseCase,
    private val sessionUseCase: SessionLocalUseCase,
    private val refreshSessionUseCase: RefreshSessionUseCase,
    private val cryptoImpl: CryptoUseCase
) : ViewModel() {

    private val _sessionLocal = MutableStateFlow<StatusResult<User>>(StatusResult.Error("Inicializando"))
    val sessionLocal = _sessionLocal.asStateFlow()

    private val _user = MutableStateFlow<StatusResult<User>?>(null)
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessageEmail = MutableStateFlow("")
    val errorMessageEmail = _errorMessageEmail.asStateFlow()

    private val _errorMessagePassword = MutableStateFlow("")
    val errorMessagePassword = _errorMessagePassword.asStateFlow()

    fun login(user: String, password: String) {

        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            when(val response = loginUseCase.invoke(UserDTO(user, password))) {
                is StatusResult.Error -> {
                    _user.value = StatusResult.Error(response.message)
                    Log.e("Login", response.message)
                    checkInputs(user, password)
                    setLoading()
                }

                is StatusResult.Success -> {
                    _user.value = response
                }
            }
        }
    }

     suspend fun saveRefreshData(file: File, token:String){
        val defRefresh = viewModelScope.async(Dispatchers.IO) {
            when (val response = refreshSessionUseCase.invoke(token)) {
                is StatusResult.Error -> StatusResult.Error(response.message)
                is StatusResult.Success -> StatusResult.Success(response.value)
            }
        }

         val defEncrypt = viewModelScope.async(Dispatchers.Default, CoroutineStart.LAZY) {
             when(defRefresh.await()){
                 is StatusResult.Error -> {
                     Log.e("login encrypt", "fallo refresh")
                 }
                 is StatusResult.Success -> {
                     when(val encryptedToken = cryptoImpl.encrypt(
                         alias = AliasTag.TOKEN.alias,
                         file = file,
                         data = (defRefresh.await() as StatusResult.Success<User>).value.token,
                     )){
                         is StatusResult.Error -> {withContext(Dispatchers.IO){
                             Log.e("login error",encryptedToken.message)
                         }}
                         is StatusResult.Success -> {
                             withContext(Dispatchers.IO){
                                 Log.e("refresh", "refresh almacenado")
                             }
                         }
                     }
                 }
             }
         }

         defEncrypt.await()
    }

    fun checkSessionLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = sessionUseCase.invoke()) {
                is StatusResult.Error -> StatusResult.Error(response.message)
                is StatusResult.Success -> {
                    _sessionLocal.value = StatusResult.Success(response.value)
                }
            }
        }
    }

    fun setLoading() {
        _isLoading.value = !isLoading.value
    }

    private fun setErrorMessageEmail(email: String) {
        _errorMessageEmail.value = email
    }

    private fun setErrorMessagePassword(password: String) {
        _errorMessagePassword.value = password

    }

    private fun checkInputs(email: String, password: String) {
        when (email.isEmpty()) {
            true -> setErrorMessageEmail("Campo requerido")
            false -> setErrorMessageEmail("Credenciales incorrectas")
        }

        when (password.isEmpty()) {
            true -> setErrorMessagePassword("Campo requerido")
            false -> setErrorMessagePassword("Credenciales incorrectas")
        }
    }

}