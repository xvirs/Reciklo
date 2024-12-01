package com.jetbrains.kmpapp.screens

import com.jetbrains.kmpapp.data.database.Database
import com.jetbrains.kmpapp.data.exampleMuseum.MuseumObject
import com.jetbrains.kmpapp.data.exampleMuseum.MuseumRepository
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListViewModel(museumRepository: MuseumRepository) : KMMViewModel() {

    val objects: StateFlow<List<MuseumObject>> =
        museumRepository.getObjects()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
