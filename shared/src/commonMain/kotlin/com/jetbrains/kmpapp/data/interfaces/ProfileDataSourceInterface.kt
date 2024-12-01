package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.data.models.ProfileDTO
import com.jetbrains.kmpapp.domain.models.StatusResult

interface ProfileDataSourceInterface {
    suspend fun getProfile(token:String): StatusResult<ProfileDTO>
}