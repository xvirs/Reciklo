package com.jetbrains.kmpapp.DI

import com.jetbrains.kmpapp.data.datasource.local.LoginDataSourceLocal
import com.jetbrains.kmpapp.data.datasource.remote.LoginDataSourceRemote
import com.jetbrains.kmpapp.data.datasource.remote.RefreshSessionDataSourceImpl
import com.jetbrains.kmpapp.data.interfaces.RefreshSessionDataSourceInterface
import com.jetbrains.kmpapp.data.repository.LoginRepository
import com.jetbrains.kmpapp.data.repository.RefreshSessionRepositoryImpl
import com.jetbrains.kmpapp.domain.interfaces.RefreshSessionInterface
import com.jetbrains.kmpapp.domain.usecase.LoginUseCase
import com.jetbrains.kmpapp.domain.usecase.RefreshSessionUseCase
import com.jetbrains.kmpapp.domain.usecase.SessionLocalUseCase
import com.jetbrains.kmpapp.domain.usecase.SessionManagerUseCase
import com.jetbrains.kmpapp.screens.splashScreen.SessionViewModel
import com.jetbrains.kmpapp.utils.crypto.CryptoInterface
import com.jetbrains.kmpapp.utils.crypto.CryptoManager
import com.jetbrains.kmpapp.utils.crypto.CryptoUseCase
import com.jetbrains.kmpapp.screens.login.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val moduleLogin = module {

    single<CryptoInterface>{ CryptoManager() }
    single{ CryptoUseCase(get()) }
    single { SessionManagerUseCase() }

    //Login
    single { LoginDataSourceLocal(get()) }
    single { LoginDataSourceRemote() }
    single { LoginRepository(get<LoginDataSourceRemote>(), get<LoginDataSourceLocal>()) }
    single { LoginUseCase(get<LoginRepository>()) }
    single { SessionLocalUseCase(get<LoginRepository>()) }

    //refresh
    single<RefreshSessionDataSourceInterface>{ RefreshSessionDataSourceImpl() }
    single<RefreshSessionInterface>{RefreshSessionRepositoryImpl(get())}
    single<RefreshSessionUseCase>{RefreshSessionUseCase(get())}
}

val viewmodelDI = module {
    viewModelOf(::SessionViewModel)
    viewModelOf(::UserViewModel)
}

