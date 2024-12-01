package com.jetbrains.kmpapp.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.reciklo.db.RecikloDataBase

actual class DataBaseDriverFactory{
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = RecikloDataBase.Schema,
            name = "RecikloDataBase.db"
        )
    }
}