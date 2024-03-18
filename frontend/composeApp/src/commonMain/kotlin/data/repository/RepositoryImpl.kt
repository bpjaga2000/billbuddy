package data.repository

import data.Repository
import data.model.dto.RegisterDto
import data.remote.ApiClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import kotlinx.coroutines.flow.flow


class RepositoryImpl : Repository {

    override suspend fun logIn(email: String, password: String) = flow {
        emit(ApiClient.httpClient.post {
            url("http:////192.168.0.104:8090/api/v1/auth/login")
            contentType(ContentType.Application.Json)
            setBody(RegisterDto(email, password))
        })
    }

}