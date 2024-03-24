package data

import data.model.dto.UserDto
import data.remote.ApiResult
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun logIn(email: String, password: String): Flow<ApiResult<UserDto>>
    suspend fun register(email: String, password: String): Flow<ApiResult<UserDto>>
}