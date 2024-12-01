package com.jetbrains.kmpapp

import android.app.Application
import com.jetbrains.kmpapp.DI.moduleDB
import com.jetbrains.kmpapp.DI.moduleLogin
import com.jetbrains.kmpapp.DI.viewmodelDI
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RecikloApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RecikloApp)
            androidLogger()
            modules(moduleDB,moduleLogin, viewmodelDI)
        }
    }
}
