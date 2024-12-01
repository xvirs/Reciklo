package com.jetbrains.kmpapp.DI

import com.jetbrains.kmpapp.data.database.DataBaseDriverFactory
import com.reciklo.db.RecikloDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val moduleDB = module {
    single {
        val db by lazy {
            RecikloDataBase(
                driver = DataBaseDriverFactory(androidContext()).createDriver()
            )
        }
        db
    }
}