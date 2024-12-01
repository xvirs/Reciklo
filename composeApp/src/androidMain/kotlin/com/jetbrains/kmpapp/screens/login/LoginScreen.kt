package com.jetbrains.kmpapp.screens.login


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.jetbrains.kmpapp.HomeActivity
import com.jetbrains.kmpapp.component.Loading
import com.jetbrains.kmpapp.component.login.BodyLogin
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User
import com.jetbrains.kmpapp.domain.usecase.SessionManagerUseCase
import com.jetbrains.kmpapp.utils.crypto.AliasTag
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.io.File

@Composable
fun LoginScreen(navController: NavHostController, finish:()->Unit) {
    val viewModel = koinViewModel<UserViewModel>()
    LaunchedEffect(Unit){
        viewModel.checkSessionLocal()
    }
    val context = LocalContext.current
    val file = File(context.filesDir, AliasTag.TOKEN.fileName)
    val sessionManager: SessionManagerUseCase = koinInject<SessionManagerUseCase>()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isPasswordVisible by remember { mutableStateOf(true) }
    var loginError by remember { mutableStateOf(false) }
    val errorMessageEmail by viewModel.errorMessageEmail.collectAsState()
    val errorMessagePassword by viewModel.errorMessagePassword.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val userState by viewModel.user.collectAsState()
    val sessionLocal by viewModel.sessionLocal.collectAsState()
    var offlineSession by remember {
        mutableStateOf(false)
    }
    var accessOfflineMode by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(sessionLocal) {
        offlineSession = when (sessionLocal) {
            is StatusResult.Error -> false
            is StatusResult.Success -> {
                true
            }
        }
    }

    LaunchedEffect(userState) {
        if (userState != null) {
            when (userState) {
                is StatusResult.Error -> {
                    loginError = true
                    sessionManager.updateStatus(SessionManager.Offline("Usuario no logueado"))
                }

                is StatusResult.Success -> {
                    Log.i("Login", "Success")
                    sessionManager.updateStatus(SessionManager.Online((userState as StatusResult.Success<User>).value))

                    scope.launch {
                        viewModel.saveRefreshData(file, (userState as StatusResult.Success<User>).value.token)
                        val intent = HomeActivity.intentProvider(context)
                        context.startActivity(intent)
                        finish()
                    }
                }

                else -> {}
            }
        }
    }

    BodyLogin(
        email = email,
        loginError = loginError,
        errorMessageEmail = errorMessageEmail,
        password = password,
        isPasswordVisible = isPasswordVisible,
        errorMessagePassword = errorMessagePassword,
        offlineSession = offlineSession,
        login = {
            when (accessOfflineMode) {
                true -> {
                    sessionManager.updateStatus(SessionManager.Offline("Usuario modo offline"))
                    val intent = HomeActivity.intentProvider(context)
                    context.startActivity(intent)
                    finish()
                }

                false -> {
                    viewModel.login(email, password)
                }
            }
        },
        accessOfflineMode = accessOfflineMode,
        emailChange = { email = it },
        passwordChange = { password = it },
        changeAccessMode = { accessOfflineMode = it }
    )
    if (isLoading.value)
        Loading(Color.Black)
}
