package com.jetbrains.kmpapp.screens

import com.jetbrains.kmpapp.data.exampleMuseum.MuseumObject
import com.jetbrains.kmpapp.data.exampleMuseum.MuseumRepository
import com.rickclephas.kmm.viewmodel.KMMViewModel
import kotlinx.coroutines.flow.Flow

class DetailViewModel(private val museumRepository: MuseumRepository) : KMMViewModel() {
    fun getObject(objectId: Int): Flow<MuseumObject?> =
        museumRepository.getObjectById(objectId)
}
