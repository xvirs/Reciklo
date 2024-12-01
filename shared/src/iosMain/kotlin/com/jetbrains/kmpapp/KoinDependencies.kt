package com.jetbrains.kmpapp

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jetbrains.kmpapp.data.database.Database
import com.jetbrains.kmpapp.data.database.DataBaseDriverFactory
import com.jetbrains.kmpapp.data.exampleMuseum.MuseumRepository
import com.jetbrains.kmpapp.di.dataModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class KoinDependencies : KoinComponent {
    val museumRepository: MuseumRepository by inject()
    val databaseRepository: Database by inject<Database>()
}
@DefaultArgumentInterop.Enabled

fun initKoin(){
    startKoin{
        modules(dataModule(DataBaseDriverFactory()))
    }
}
