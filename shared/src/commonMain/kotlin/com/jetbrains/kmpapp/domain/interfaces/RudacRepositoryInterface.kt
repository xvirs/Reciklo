package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.domain.models.Rudac
import com.jetbrains.kmpapp.domain.models.StatusResult

interface RudacRepositoryInterface {
    suspend fun getRudac(codRudac : String) : StatusResult<Rudac>
}