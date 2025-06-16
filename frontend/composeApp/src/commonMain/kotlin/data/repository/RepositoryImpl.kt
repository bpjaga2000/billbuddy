package data.repository

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.adapter.primitive.FloatColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.russhwolf.settings.get
import data.Repository
import data.SyncStatus
import data.model.Balance
import data.model.dto.LoginDto
import data.model.dto.SyncDto
import data.model.dto.UserDto
import data.remote.ApiClient
import data.remote.ApiResult
import dev.bpj4.billbuddy.queries.GroupMemberQueriesQueries
import dev.bpj4.billbuddy.queries.GroupQueriesQueries
import dev.bpj4.billbuddy.queries.SpendQueriesQueries
import dev.bpj4.billbuddy.queries.SpendSplitQueriesQueries
import dev.bpj4.billbuddy.queries.UserQueriesQueries
import dev.bpj4.billbuddy.tableandmigrations.GroupMembers
import dev.bpj4.billbuddy.tableandmigrations.Groups
import dev.bpj4.billbuddy.tableandmigrations.SpendSplits
import dev.bpj4.billbuddy.tableandmigrations.Spends
import dev.bpj4.billbuddy.tableandmigrations.Users
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.parameters
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.koin.core.logger.Logger
import utils.DataStore
import utils.getSqlDriver

class RepositoryImpl : Repository {

    private val db = getSqlDriver()!!

    override suspend fun logIn(email: String, password: String) = flow<ApiResult<UserDto>> {
        runBlocking {
            delay(3000)
        }
        emit(ApiResult.loading())
        with(ApiClient.httpClient.post {
            url("http:////92.119.126.127:8090/api/v1/auth/login")
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
            url("http:////92.119.126.127:8090/api/v1/auth/register")
            contentType(ContentType.Application.Json)
            setBody(LoginDto(email, password))
        }) {
            if (status.value in 200..299)
                emit(ApiResult.success(body()))
            else
                emit(ApiResult.error(body() as String?))
        }
    }

    override suspend fun sync(userId: String, lastSyncTime: Long?) = flow<ApiResult<SyncDto>> {
        emit(ApiResult.loading())
        with(ApiClient.httpClient.get {
            headers {
                print("Bearer ${DataStore.settings["token"] ?: " "}")
                append(HttpHeaders.Authorization, "Bearer ${DataStore.settings["token"] ?: " "}")
            }
            contentType(ContentType.Application.Json)
            url("http", "92.119.126.127", 8090, "api/v1/sync") {
                parameters.append("id", userId)
                if (lastSyncTime != null)
                    parameters.append("lastSyncTime", lastSyncTime.toString())
            }
        }) {
            if (status.value in 200..299)
                emit(ApiResult.success(body()))
            else
                emit(ApiResult.error(body() as String?))
        }
    }

    override suspend fun saveSyncData(data: SyncDto) = flow {
        try {
            with(data) {
                users.forEach {
                    UserQueriesQueries(db, Users.Adapter(IntColumnAdapter)).insertUsers(
                        it.id,
                        it.name,
                        "",
                        it.email,
                        it.mobileCountryCode,
                        it.phone,
                        "",
                        "",
                        it.createdAtFrontend,
                        it.updatedAtFrontend,
                        it.deletedAtFrontend,
                        SyncStatus.SYNCED.value
                    )
                }
                groups.forEach {
                    GroupQueriesQueries(
                        db, Groups.Adapter(
                            EnumColumnAdapter(),
                            IntColumnAdapter
                        )
                    ).insertGroups(
                        it.id,
                        it.name,
                        it.tag,
                        it.ownerId,
                        "",
                        "",
                        "",
                        it.createdAtFrontend,
                        it.updatedAtFrontend,
                        it.deletedAtFrontend,
                        SyncStatus.SYNCED.value
                    )
                }
                groupMembers.forEach {
                    GroupMemberQueriesQueries(
                        db, GroupMembers.Adapter(
                            IntColumnAdapter
                        )
                    ).insertGroupMembers(
                        it.id,
                        it.userId,
                        it.groupId,
                        it.createdBy,
                        it.updatedBy,
                        it.deletedBy,
                        it.createdAt,
                        it.updatedAt,
                        it.deletedAt,
                        SyncStatus.SYNCED.value
                    )
                }
                spends.forEach {
                    SpendQueriesQueries(
                        db, Spends.Adapter(
                            FloatColumnAdapter,
                            EnumColumnAdapter(),
                            IntColumnAdapter
                        )
                    ).insertSpends(
                        it.id,
                        it.name,
                        it.totalAmount,
                        it.isPayback,
                        it.tag,
                        it.groupId,
                        it.spentAt,
                        it.createdBy,
                        it.updatedBy,
                        it.deletedBy,
                        it.createdAtFrontend,
                        it.updatedAtFrontend,
                        it.deletedAtFrontend,
                        SyncStatus.SYNCED.value
                    )
                }
                spendSplit.forEach {
                    SpendSplitQueriesQueries(
                        db, SpendSplits.Adapter(
                            FloatColumnAdapter,
                            IntColumnAdapter
                        )
                    ).insertSpendSplits(
                        it.id,
                        it.userId,
                        it.spendId,
                        it.lentOrBorrowed.toLong(),
                        it.splitType.toLong(),
                        it.value,
                        it.createdBy,
                        it.updatedBy,
                        it.deletedBy,
                        it.createdAtFrontend,
                        it.updatedAtFrontend,
                        it.deletedAtFrontend,
                        SyncStatus.SYNCED.value
                    )
                }
            }
            emit(true)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(false)
        }
    }

    override suspend fun getGroups() = flow {
        emit(
            GroupQueriesQueries(
                db,
                Groups.Adapter(EnumColumnAdapter(), IntColumnAdapter)
            ).selectAllGroups()
                .executeAsList()
        )
    }

    override suspend fun getFriendBalances() = flow<List<Balance>> {
        emit(listOf())
    }

}