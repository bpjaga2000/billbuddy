package data

import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun logIn(email: String, password: String): Flow<HttpResponse>
}