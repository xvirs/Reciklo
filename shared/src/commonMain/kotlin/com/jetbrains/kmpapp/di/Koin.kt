package com.jetbrains.kmpapp.di

import com.jetbrains.kmpapp.data.database.Database
import com.jetbrains.kmpapp.data.database.DataBaseDriverFactory
import com.jetbrains.kmpapp.data.exampleMuseum.InMemoryMuseumStorage
import com.jetbrains.kmpapp.data.exampleMuseum.KtorMuseumApi
import com.jetbrains.kmpapp.data.exampleMuseum.MuseumApi
import com.jetbrains.kmpapp.data.exampleMuseum.MuseumRepository
import com.jetbrains.kmpapp.data.exampleMuseum.MuseumStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

fun dataModule(recikloDatabase: DataBaseDriverFactory) = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }
        }
    }
    single<Database> { Database(recikloDatabase) }
    single<MuseumApi> { KtorMuseumApi(get()) }
    single<MuseumStorage> { InMemoryMuseumStorage() }
    single {
        MuseumRepository(get(), get()).apply {
            initialize()
        }
    }
}
/*
@DefaultArgumentInterop.Enabled
fun initKoinDI(modules: List<Module> = emptyList(), dataBase: DataBaseDriverFactory) {
    startKoin {
        modules(
            dataModule(dataBase),
            *modules.toTypedArray(),
        )
    }
}*/
