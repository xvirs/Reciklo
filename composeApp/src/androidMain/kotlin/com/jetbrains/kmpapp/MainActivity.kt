package com.jetbrains.kmpapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.jetbrains.kmpapp.component.LottieLoadingAnimation
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User
import com.jetbrains.kmpapp.domain.usecase.SessionManagerUseCase
import com.jetbrains.kmpapp.navigation.AppScreens
import com.jetbrains.kmpapp.screens.login.LoginScreen
import com.jetbrains.kmpapp.screens.splashScreen.SessionViewModel
import com.jetbrains.kmpapp.utils.crypto.AliasTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI
import java.io.File

class MainActivity : ComponentActivity() {

    companion object{
        fun intentProvider(context: Context) = Intent(context, MainActivity::class.java)
    }

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewmodel: SessionViewModel by inject<SessionViewModel>()
        val file = File(filesDir, AliasTag.TOKEN.fileName)
        val sessionUseCase = inject<SessionManagerUseCase>()

        lifecycleScope.launch {
            viewmodel.checkSession(file)
        }

        lifecycleScope.launch(Dispatchers.IO) {
            viewmodel.statusSession.collect{ status ->
                when(status){
                    is StatusResult.Error -> {
                        Log.e("login", status.message)
                    }
                    is StatusResult.Success -> {
                        sessionUseCase.value.updateStatus(SessionManager.Online(User(id = 0, token = status.value)))
                        startActivity(HomeActivity.intentProvider(this@MainActivity))
                        finish()
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Main){
            viewmodel.eventNavigation.collect{ status ->
                    setContent{
                        KoinAndroidContext {
                            if (status) {
                                LoginEvent{
                                    this@MainActivity.finish()
                                    viewmodel.navigationReady()
                                }
                            }else
                                LottieLoadingAnimation()
                        }
                    }
            }
        }
    }
}

@Composable
fun LoginEvent(finish:()->Unit) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login"){
        navigation(startDestination = AppScreens.LoginScreen.route, route = "login") {
            composable(AppScreens.LoginScreen.route) {
                LoginScreen(navController = navController){
                    finish()
                }
            }
        }
    }
}

