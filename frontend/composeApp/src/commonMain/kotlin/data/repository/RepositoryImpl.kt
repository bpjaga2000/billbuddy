package data.repository

import data.Repository
import data.model.dto.LoginDto
import data.model.dto.UserDto
import data.remote.ApiClient
import data.remote.ApiResult
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class RepositoryImpl : Repository {

    override suspend fun logIn(email: String, password: String) = flow<ApiResult<UserDto>> {
        runBlocking {
            delay(3000)
        }
        emit(ApiResult.loading())
        with(ApiClient.httpClient.post {
            url("http:////192.168.0.104:8090/api/v1/auth/login")
            contentType(ContentType.Application.Json)
            setBody(LoginDto(email, password))
        }) {
            if (status.value in 200..299)
                emit(ApiResult.success(body()))
            else
                emit(ApiResult.error(body() as String?))
        }
    }

    override suspend fun register(email: String, password: String) = flow<ApiResult<UserDto>> {
        emit(ApiResult.loading())
        with(ApiClient.httpClient.post {
            url("http:////192.168.0.104:8090/api/v1/auth/register")
            contentType(ContentType.Application.Json)
            setBody(LoginDto(email, password))
        }) {
            if (status.value in 200..299)
                emit(ApiResult.success(body()))
            else
                emit(ApiResult.error(body() as String?))
        }
    }

}