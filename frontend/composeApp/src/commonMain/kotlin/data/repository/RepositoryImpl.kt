package data.repository

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import constants.LentOrBorrowed
import constants.SplitType
import constants.UserRoles
import data.GroupTags
import data.Repository
import data.SpendTags
import data.SyncStatus
import data.model.Balance
import data.model.EditSpendDetails
import data.model.EditSpendTabDetails
import data.model.GroupMember
import data.model.SpendWithSplit
import data.model.dto.GroupDto
import data.model.dto.GroupResponseDto
import data.model.dto.LoginDto
import data.model.dto.ProfileDto
import data.model.dto.ProfileUpdateDto
import data.model.dto.SyncDto
import data.model.dto.UserDto
import data.model.dto.UserIdListDto
import data.remote.ApiClient
import data.remote.ApiResult
import dev.bpj4.billbuddy.queries.GroupMemberQueriesQueries
import dev.bpj4.billbuddy.queries.GroupQueriesQueries
import dev.bpj4.billbuddy.queries.MiscQueriesQueries
import dev.bpj4.billbuddy.queries.SpendQueriesQueries
import dev.bpj4.billbuddy.queries.SpendSplitQueriesQueries
import dev.bpj4.billbuddy.queries.UserQueriesQueries
import dev.bpj4.billbuddy.tableandmigrations.Groups
import dev.bpj4.billbuddy.tableandmigrations.SpendSplits
import dev.bpj4.billbuddy.tableandmigrations.Spends
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.voxkit.kotlin.nanoid.NanoId
import kotlinx.coroutines.flow.flow
import utils.DataStore
import utils.getSqlDriver
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class RepositoryImpl : Repository {

    private val db = getSqlDriver()!!
    private val currentUserId = DataStore.settings.get<String>("id") ?: ""

    override suspend fun logIn(email: String, password: String) = flow<ApiResult<UserDto>> {
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

    override suspend fun sync(userId: String) = flow<ApiResult<SyncDto>> {
        emit(ApiResult.loading())
        with(ApiClient.httpClient.get {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${DataStore.settings["token"] ?: " "}")
            }
            contentType(ContentType.Application.Json)
            url("http", "92.119.126.127", 8090, "api/v1/sync") {
                parameters.append("id", userId)
                DataStore.settings.get<Long>("lastSyncTime")?.let { lastSyncTime ->
                    parameters.append("lastSyncTime", lastSyncTime.toString())
                }
            }
        }) {
            if (status.value in 200..299) {
                emit(ApiResult.success(body()))
                DataStore.settings.set<Long>("lastSyncTime", Clock.System.now().epochSeconds)
            } else
                emit(ApiResult.error(body() as String?))
        }
    }

    override suspend fun saveSyncData(data: SyncDto) = flow {
        try {
            with(data) {
                users.forEach {
                    UserQueriesQueries(db).insertUsers(
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
                        it.deletedAtFrontend
                    )
                }
                groups.forEach {
                    GroupQueriesQueries(
                        db, Groups.Adapter(
                            EnumColumnAdapter()
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
                        it.deletedAtFrontend
                    )
                }
                groupMembers.forEach {
                    GroupMemberQueriesQueries(
                        db
                    ).insertGroupMembers(
                        it.id,
                        it.userId,
                        it.groupId,
                        it.createdBy,
                        it.updatedBy,
                        it.deletedBy,
                        it.createdAt,
                        it.updatedAt,
                        it.deletedAt
                    )
                }
                spends.forEach {
                    SpendQueriesQueries(
                        db, Spends.Adapter(
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
                Groups.Adapter(EnumColumnAdapter())
            ).selectAllGroups()
                .executeAsList()
        )
    }

    override suspend fun getGroupById(id: String) = flow {
        emit(
            GroupQueriesQueries(
                db,
                Groups.Adapter(EnumColumnAdapter())
            ).getGroupById(id).executeAsOne()
        )
    }

    override suspend fun getFriendBalances() = flow<List<Balance>> {
        emit(listOf())
    }

    override suspend fun createGroup(groupName: String, groupTag: GroupTags) =
        flow {
            emit(ApiResult.loading())
            with(ApiClient.httpClient.post {
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer ${DataStore.settings["token"] ?: " "}"
                    )
                }
                contentType(ContentType.Application.Json)
                url("http", "92.119.126.127", 8090, "api/v1/group")
                setBody(GroupDto(groupName, groupTag, DataStore.settings.get<String>("id")!!))
            }) {
                if (status.value in 200..299) {
                    val body = body<GroupResponseDto>()
                    GroupQueriesQueries(
                        db,
                        Groups.Adapter(EnumColumnAdapter())
                    ).insertGroups(
                        body.id,
                        body.name,
                        body.tag,
                        body.ownerId,
                        body.ownerId,
                        body.ownerId,
                        "",
                        0, 0, 0,
                    )
                    emit(ApiResult.success(body))
                } else
                    emit(ApiResult.error(body() as String?))
            }
        }

    override suspend fun updateGroup(groupId: String, groupDto: GroupDto) =
        flow {
            emit(ApiResult.loading())
            with(ApiClient.httpClient.put {
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer ${DataStore.settings["token"] ?: " "}"
                    )
                }
                contentType(ContentType.Application.Json)
                url("http", "92.119.126.127", 8090, "api/v1/group/$groupId")
                setBody(groupDto)
            }) {
                if (status.value in 200..299) {
                    val body = body<GroupResponseDto>()
                    GroupQueriesQueries(
                        db,
                        Groups.Adapter(EnumColumnAdapter())
                    ).insertGroups(
                        body.id,
                        body.name,
                        body.tag,
                        body.ownerId,
                        body.ownerId,
                        body.ownerId,
                        "",
                        0, 0, 0,
                    )
                    emit(ApiResult.success(body))
                } else
                    emit(ApiResult.error(body() as String?))
            }
        }

    override suspend fun getSpendAndSplitForGroup(groupId: String) = flow {
        val spendWithSplitList: ArrayList<SpendWithSplit> = arrayListOf()
        val spends = SpendQueriesQueries(
            db,
            Spends.Adapter(EnumColumnAdapter(), IntColumnAdapter)
        ).getSpendsForGroup(groupId).executeAsList()
        spends.forEach {
            spendWithSplitList.add(
                SpendWithSplit(
                    it, SpendSplitQueriesQueries(
                        db,
                        SpendSplits.Adapter(IntColumnAdapter)
                    ).getSplitForSpend(it.id).executeAsList()
                )
            )
        }
        emit(spendWithSplitList)
    }

    override suspend fun getSpendAndSplitWithSpendId(spendId: String) = flow {
        val spend = SpendQueriesQueries(
            db,
            Spends.Adapter(EnumColumnAdapter(), IntColumnAdapter)
        ).getSpendWithId(spendId).executeAsOne()
        emit(
            SpendWithSplit(
                spend, SpendSplitQueriesQueries(
                    db,
                    SpendSplits.Adapter(IntColumnAdapter)
                ).getSplitForSpend(spend.id).executeAsList()
            )
        )
    }

    override suspend fun deleteSpend(spendId: String) = flow {
        emit(false)
        SpendSplitQueriesQueries(
            db,
            SpendSplits.Adapter(IntColumnAdapter)
        ).deleteSpendSplit(
            currentUserId,
            Clock.System.now().epochSeconds,
            SyncStatus.LOCAL.value,
            spendId
        )
        SpendQueriesQueries(
            db,
            Spends.Adapter(EnumColumnAdapter(), IntColumnAdapter)
        ).deleteSpend(
            currentUserId,
            Clock.System.now().epochSeconds,
            SyncStatus.LOCAL.value,
            spendId
        )
        emit(true)
    }

    override suspend fun getUserNameFromId(id: String) =
        flow {
            emit(
                UserQueriesQueries(db).getUserFromId(id)
                    .executeAsList()[0].name
            )
        }

    override suspend fun getGroupMemberDetails(groupId: String) = flow {
        emit(UserQueriesQueries(db).getMemberDetailsOfGroup(groupId).executeAsList().map { it ->
            GroupMember(it.groupMemberId, it.id, it.name)
        })
    }

    override suspend fun removeMemberFromGroup(userIds: List<String>, groupId: String) =
        flow {
            emit(ApiResult.loading())
            with(ApiClient.httpClient.post {
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer ${DataStore.settings["token"] ?: " "}"
                    )
                }
                contentType(ContentType.Application.Json)
                url("http", "92.119.126.127", 8090, "api/v1/group/manage/remove/$groupId")
                setBody(UserIdListDto(userIds, currentUserId))
            }) {
                if (status.value in 200..299) {
                    val body = body<GroupResponseDto>()
                    body.members.forEach {
                        GroupMemberQueriesQueries(db).insertGroupMembers(
                            it.id,
                            it.userId,
                            it.groupId,
                            it.createdBy,
                            it.updatedBy,
                            it.deletedBy,
                            it.createdAt,
                            it.updatedAt,
                            it.deletedAt
                        )
                    }
                    emit(ApiResult.success(body))
                } else
                    emit(ApiResult.error(body() as String?))
            }
        }

    override suspend fun searchFriends(searchTag: String) = flow {
        emit(UserQueriesQueries(db).searchUsers(searchTag).executeAsList().map {
            ProfileDto(
                it.id,
                it.name,
                it.email,
                it.mobileCountryCode,
                it.phone,
                it.createdAt,
                it.updatedAt,
                it.deletedAt,
                it.createdAt,
                it.updatedAt,
                it.deletedAt,
            )
        })
    }

    override suspend fun searchFriendsOnline(searchTag: String) = flow {
        emit(ApiResult.loading())
        with(ApiClient.httpClient.get {
            headers {
                append(
                    HttpHeaders.Authorization,
                    "Bearer ${DataStore.settings["token"] ?: " "}"
                )
            }
            contentType(ContentType.Application.Json)
            url("http", "92.119.126.127", 8090, "api/v1/search")
            parameter("query", searchTag)
        }) {
            if (status.value in 200..299) {
                val body = body<List<ProfileDto>>()
                emit(ApiResult.success(body))
            } else
                emit(ApiResult.error(body() as String?))
        }
    }

    override suspend fun addUsers(profiles: List<ProfileDto>) = flow {
        emit(false)
        profiles.forEach {
            UserQueriesQueries(db).insertUsers(
                it.id,
                it.name,
                "",
                it.email,
                it.mobileCountryCode,
                it.phone,
                UserRoles.FREE,
                "",
                it.createdAt,
                it.updatedAt,
                it.deletedAt
            )
        }
        emit(true)
    }

    override suspend fun addGroupMembers(
        userIds: List<String>,
        groupId: String
    ) = flow {
        emit(ApiResult.loading())
        with(ApiClient.httpClient.post {
            headers {
                append(
                    HttpHeaders.Authorization,
                    "Bearer ${DataStore.settings["token"] ?: " "}"
                )
            }
            contentType(ContentType.Application.Json)
            url("http", "92.119.126.127", 8090, "api/v1/group/manage/add/$groupId")
            setBody(UserIdListDto(userIds, currentUserId))
        }) {
            if (status.value in 200..299) {
                val body = body<GroupResponseDto>()
                body.members.forEach {
                    GroupMemberQueriesQueries(db).insertGroupMembers(
                        it.id,
                        it.userId,
                        it.groupId,
                        it.createdBy,
                        it.updatedBy,
                        it.deletedBy,
                        it.createdAt,
                        it.updatedAt,
                        it.deletedAt
                    )
                }
                emit(ApiResult.success(body))
            } else
                emit(ApiResult.error(body() as String?))
        }
    }

    override suspend fun getCurrentUser() = flow {
        emit(
            UserQueriesQueries(db).getUserFromId(currentUserId)
                .executeAsOne()
        )
    }

    override suspend fun updateProfile(profileUpdateDto: ProfileUpdateDto) =
        flow {
            emit(ApiResult.loading())
            with(ApiClient.httpClient.post {
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer ${DataStore.settings["token"] ?: " "}"
                    )
                }
                contentType(ContentType.Application.Json)
                url("http", "92.119.126.127", 8090, "api/v1/profile/${profileUpdateDto.id}")
                setBody(profileUpdateDto)
            }) {
                if (status.value in 200..299) {
                    val body = body<ProfileDto>()
                    UserQueriesQueries(db).insertUsers(
                        body.id,
                        body.name,
                        "",
                        body.email,
                        body.mobileCountryCode,
                        body.phone,
                        UserRoles.FREE,
                        "",
                        body.createdAt,
                        body.updatedAt,
                        body.deletedAt
                    )
                    emit(ApiResult.success(body))
                } else
                    emit(ApiResult.error(body() as String?))
            }
        }

    override suspend fun logout() = flow {
        emit(ApiResult.loading())
        with(ApiClient.httpClient.get {
            headers {
                append(
                    HttpHeaders.Authorization,
                    "Bearer ${DataStore.settings["token"] ?: " "}"
                )
            }
            contentType(ContentType.Application.Json)
            url(
                "http",
                "92.119.126.127",
                8090,
                "api/v1/auth/logout/${DataStore.settings["id"] ?: " "}"
            )
        }) {
            if (status.value in 200..299) {
                val body = body<Unit>()
                emit(ApiResult.success(body))
            } else
                emit(ApiResult.error(body() as String?))
        }
    }

    override suspend fun clearDb() {
        MiscQueriesQueries(db).clearData()
    }

    override suspend fun saveSpend(
        spendName: String,
        amount: String,
        spendTags: SpendTags,
        groupId: String,
        spentAt: Long
    ) = flow {
        emit("")
        val spendId = NanoId.generate()
        SpendQueriesQueries(
            db,
            Spends.Adapter(EnumColumnAdapter(), IntColumnAdapter)
        ).insertSpends(
            spendId,
            spendName,
            amount.toDouble(),
            false,
            spendTags,
            groupId,
            spentAt,
            currentUserId,
            currentUserId,
            null,
            Clock.System.now().epochSeconds,
            Clock.System.now().epochSeconds,
            null,
            SyncStatus.LOCAL.value
        )
        emit(spendId)
    }

    fun saveSpendSplits(
        lends: List<EditSpendDetails>,
        borrows: List<EditSpendTabDetails>,
        spendId: String,
        groupId: String,
        splitType: Int
    ) = flow {//TODO
        emit(false)
        val spendSplitTable = SpendSplitQueriesQueries(
            db,
            SpendSplits.Adapter(IntColumnAdapter)
        )
        spendSplitTable.transaction {
            afterRollback {
                SpendQueriesQueries(
                    db,
                    Spends.Adapter(EnumColumnAdapter(), IntColumnAdapter)
                ).removeSpend(groupId)
            }
            lends.forEach {
                spendSplitTable.insertSpendSplits(
                    NanoId.generate(),
                    it.userId,
                    spendId,
                    LentOrBorrowed.LENT.toLong(),
                    SplitType.AMOUNT.toLong(),
                    it.value.value.toDouble(),
                    currentUserId,
                    currentUserId,
                    null,
                    Clock.System.now().epochSeconds,
                    Clock.System.now().epochSeconds,
                    null,
                    SyncStatus.LOCAL.value
                )
            }
            borrows.forEach {
                spendSplitTable.insertSpendSplits(
                    NanoId.generate(),
                    it.userId,
                    spendId,
                    LentOrBorrowed.BORROWED.toLong(),
                    splitType.toLong(),
                    it.value.value.toDouble(),
                    currentUserId,
                    currentUserId,
                    null,
                    Clock.System.now().epochSeconds,
                    Clock.System.now().epochSeconds,
                    null,
                    SyncStatus.LOCAL.value
                )
            }
        }
        emit(true)
    }

    override suspend fun updateSpend(
        spend: Spends
    ) = flow {
        emit(false)
        SpendQueriesQueries(
            db,
            Spends.Adapter(EnumColumnAdapter(), IntColumnAdapter)
        ).insertSpends(
            spend.id,
            spend.name,
            spend.totalAmount,
            spend.isPayback,
            spend.tag,
            spend.groupId,
            spend.spentAt,
            spend.createdBy,
            spend.updatedBy,
            spend.deletedBy,
            spend.createdAt,
            spend.updatedAt,
            spend.deletedAt,
            SyncStatus.LOCAL.value
        )
        emit(true)
    }

    override suspend fun updateSpendSplits(
        lends: List<EditSpendDetails>,
        borrows: List<EditSpendTabDetails>,
        splitType: Int,
        spend: Spends
    ) = flow {//TODO
        emit(false)
        val spendSplitTable = SpendSplitQueriesQueries(
            db,
            SpendSplits.Adapter(IntColumnAdapter)
        )
        spendSplitTable.transaction {
            afterRollback {
                SpendQueriesQueries(
                    db,
                    Spends.Adapter(EnumColumnAdapter(), IntColumnAdapter)
                ).insertSpends(
                    spend.id,
                    spend.name,
                    spend.totalAmount,
                    spend.isPayback,
                    spend.tag,
                    spend.groupId,
                    spend.spentAt,
                    spend.createdBy,
                    spend.updatedBy,
                    spend.deletedBy,
                    spend.createdAt,
                    spend.updatedAt,
                    spend.deletedAt,
                    spend.status
                )
            }
            spendSplitTable.deleteSpendSplit(
                currentUserId,
                Clock.System.now().epochSeconds,
                SyncStatus.LOCAL.value,
                spend.id
            )
            lends.forEach {
                spendSplitTable.insertSpendSplits(
                    NanoId.generate(),
                    it.userId,
                    spend.id,
                    LentOrBorrowed.LENT.toLong(),
                    SplitType.AMOUNT.toLong(),
                    it.value.value.toDouble(),
                    currentUserId,
                    currentUserId,
                    null,
                    Clock.System.now().epochSeconds,
                    Clock.System.now().epochSeconds,
                    null,
                    SyncStatus.LOCAL.value
                )
            }
            borrows.forEach {
                spendSplitTable.insertSpendSplits(
                    NanoId.generate(),
                    it.userId,
                    spend.id,
                    LentOrBorrowed.BORROWED.toLong(),
                    splitType.toLong(),
                    it.value.value.toDouble(),
                    currentUserId,
                    currentUserId,
                    null,
                    Clock.System.now().epochSeconds,
                    Clock.System.now().epochSeconds,
                    null,
                    SyncStatus.LOCAL.value
                )
            }
        }
        emit(true)
    }


}