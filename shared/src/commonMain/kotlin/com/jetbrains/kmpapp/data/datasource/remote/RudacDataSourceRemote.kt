package com.jetbrains.kmpapp.data.datasource.remote

import com.jetbrains.kmpapp.data.interfaces.RudacDataSourceRemoteInterface
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.domain.models.Rudac
import com.jetbrains.kmpapp.domain.models.StatusResult
import io.ktor.client.call.body
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class RudacDataSourceRemote() : RudacDataSourceRemoteInterface {

    companion object {
        const val BASE_URL: String = "http://reciklo.artekium.com:3100/desarmadero/"
        val baseClient: BaseClient = BaseClient()
    }

    override suspend fun getRudac(codRudac: String): StatusResult<Rudac> =
        withContext(Dispatchers.IO) {
            when (val res = fetchRudac("$codRudac", "fail to send petition")) {
                is StatusResult.Error -> StatusResult.Error(res.message)
                is StatusResult.Success -> StatusResult.Success(res.value)
            }
        }

    private suspend fun fetchRudac(
        url: String,
        messageError: String
    ): StatusResult<Rudac> {
        val httpResult = baseClient.get(BASE_URL + url, messageError, "")
        try {
            httpResult.httpResponse?.let {
                return StatusResult.Success(value = it.body())
            }
            return StatusResult.Error(httpResult.errorMessage)
        } catch (e: JsonConvertException) {
            return StatusResult.Error(messageError)
        }
    }
}