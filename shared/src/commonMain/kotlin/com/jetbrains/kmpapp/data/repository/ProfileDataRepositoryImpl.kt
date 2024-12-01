package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.interfaces.ProfileDataSourceInterface
import com.jetbrains.kmpapp.data.mappers.toModel
import com.jetbrains.kmpapp.domain.interfaces.ProfileDataRepositoryInterface
import com.jetbrains.kmpapp.domain.models.Profile
import com.jetbrains.kmpapp.domain.models.StatusResult

class ProfileDataRepositoryImpl(private val dataSource : ProfileDataSourceInterface):ProfileDataRepositoryInterface {
    override suspend fun getProfile(token: String): StatusResult<Profile> {
        return when(val response = dataSource.getProfile(token)){
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> {StatusResult.Success(response.value.toModel())}
        }
    }
}