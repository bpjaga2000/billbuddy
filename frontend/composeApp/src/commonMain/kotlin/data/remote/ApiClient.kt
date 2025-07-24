package data.remote

import com.russhwolf.settings.get
import data.Repository
import di.koin
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.http.buildUrl
import io.ktor.http.encodedPath
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {

    val repository by koin.inject<Repository>()

    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
            buildUrl {
                protocol = URLProtocol.HTTP
                host = "92.119.126.127"
                port = 8090
                encodedPath = "api/v1"
            }
            headers {
                append("Content-Type", "application/json")
                repository.getSettings().get<String>("token")?.let {
                    append("Authorization", "Bearer $it")
                }
                append("Access-Control-Allow-Origin", "http:////92.119.126.127:8090");
            }
        }
        Logging {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

}