package com.jetbrains.kmpapp.data.database

import com.reciklo.db.RecikloDataBase

class Database(db: DataBaseDriverFactory) {
    private val dataBase = RecikloDataBase(db.createDriver())

    private val dbQueries = dataBase.productQueries

    fun getAllProducts(): List<String> {
        return dbQueries.getAll().executeAsList().map { productEntity -> productEntity.product!! }
    }

    fun setProduct(product:String){
        dbQueries.inserProduct(product)
    }

}