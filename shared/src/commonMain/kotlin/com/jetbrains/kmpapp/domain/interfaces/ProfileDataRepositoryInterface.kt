package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.domain.models.Profile
import com.jetbrains.kmpapp.domain.models.StatusResult

interface ProfileDataRepositoryInterface {
    suspend fun getProfile(token:String): StatusResult<Profile>
}