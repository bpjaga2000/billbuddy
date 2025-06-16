package data

import data.model.Balance
import data.model.dto.SyncDto
import data.model.dto.UserDto
import data.remote.ApiResult
import dev.bpj4.billbuddy.tableandmigrations.Groups
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun logIn(email: String, password: String): Flow<ApiResult<UserDto>>
    suspend fun register(email: String, password: String): Flow<ApiResult<UserDto>>
    suspend fun sync(userId: String, lastSyncTime: Long? = null): Flow<ApiResult<SyncDto>>
    suspend fun saveSyncData(data: SyncDto): Flow<Boolean>
    suspend fun getGroups(): Flow<List<Groups>>
    suspend fun getFriendBalances(): Flow<List<Balance>>
}