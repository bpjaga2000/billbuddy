package data

import data.model.Balance
import data.model.GroupMember
import data.model.SpendWithSplit
import data.model.dto.GroupDto
import data.model.dto.GroupResponseDto
import data.model.dto.ProfileDto
import data.model.dto.ProfileUpdateDto
import data.model.dto.SyncDto
import data.model.dto.UserDto
import data.remote.ApiResult
import dev.bpj4.billbuddy.tableandmigrations.Groups
import dev.bpj4.billbuddy.tableandmigrations.Users
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun logIn(email: String, password: String): Flow<ApiResult<UserDto>>
    suspend fun register(email: String, password: String): Flow<ApiResult<UserDto>>
    suspend fun sync(userId: String): Flow<ApiResult<SyncDto>>
    suspend fun saveSyncData(data: SyncDto): Flow<Boolean>
    suspend fun getGroups(): Flow<List<Groups>>
    suspend fun getFriendBalances(): Flow<List<Balance>>
    suspend fun createGroup(
        groupName: String,
        groupTag: GroupTags
    ): Flow<ApiResult<GroupResponseDto>>

    suspend fun getSpendAndSplitForGroup(groupId: String): Flow<List<SpendWithSplit>>
    suspend fun getUserNameFromId(id: String): Flow<String>
    suspend fun getGroupById(id: String): Flow<Groups>
    suspend fun updateGroup(groupId: String, groupDto: GroupDto): Flow<ApiResult<GroupResponseDto>>
    suspend fun getGroupMemberDetails(groupId: String): Flow<List<GroupMember>>
    suspend fun removeMemberFromGroup(userIds: List<String>, groupId: String): Flow<ApiResult<GroupResponseDto>>
    suspend fun searchFriends(searchTag: String): Flow<List<ProfileDto>>
    suspend fun searchFriendsOnline(searchTag: String): Flow<ApiResult<List<ProfileDto>>>
    suspend fun addUsers(profiles: List<ProfileDto>): Flow<Boolean>
    suspend fun addGroupMembers(
        userIds: List<String>,
        groupId: String
    ): Flow<ApiResult<GroupResponseDto>>

    suspend fun getCurrentUser(): Flow<Users>
    suspend fun updateProfile(profileUpdateDto: ProfileUpdateDto): Flow<ApiResult<ProfileDto>>
    suspend fun logout(): Flow<ApiResult<Unit>>
    suspend fun clearDb()
    suspend fun saveSpend(
        spendName: String,
        amount: String,
        spendTags: SpendTags,
        groupId: String,
        spentBy: String,
        spentAt: Long
    ): Flow<String>

    suspend fun getSpendAndSplitWithSpendId(spendId: String): Flow<SpendWithSplit>
}