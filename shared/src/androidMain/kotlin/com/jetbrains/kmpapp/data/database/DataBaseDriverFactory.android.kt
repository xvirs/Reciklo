package com.jetbrains.kmpapp.data.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.reciklo.db.RecikloDataBase

actual class DataBaseDriverFactory(private val context: Context){
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = RecikloDataBase.Schema,
            context = context,
            name = "RecikloDataBase.db"
        )
    }
}