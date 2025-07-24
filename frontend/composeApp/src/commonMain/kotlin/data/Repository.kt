package data

import com.russhwolf.settings.Settings
import data.model.EditSpendTabDetails
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
import dev.bpj4.billbuddy.tableandmigrations.Spends
import dev.bpj4.billbuddy.tableandmigrations.Users
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getSettings(): Settings//todo remove
    suspend fun logIn(email: String, password: String): Flow<ApiResult<UserDto>>
    suspend fun register(email: String, password: String): Flow<ApiResult<UserDto>>
    suspend fun sync(): Flow<ApiResult<SyncDto>>
    suspend fun saveSyncData(data: SyncDto): Flow<Boolean>
    suspend fun getAllGroups(): Flow<List<Groups>>
    suspend fun createGroup(
        groupName: String,
        groupTag: GroupTags
    ): Flow<ApiResult<GroupResponseDto>>

    suspend fun getSpendAndSplitForGroup(groupId: String): Flow<List<SpendWithSplit>>
    suspend fun getUserNameFromId(id: String): Flow<String>
    suspend fun getGroupById(id: String): Flow<Groups>
    suspend fun updateGroup(groupId: String, groupDto: GroupDto): Flow<ApiResult<GroupResponseDto>>
    suspend fun getGroupMemberDetails(groupId: String): Flow<List<GroupMember>>
    suspend fun removeMemberFromGroup(
        userIds: List<String>,
        groupId: String
    ): Flow<ApiResult<GroupResponseDto>>

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
    suspend fun clearDb(): Flow<Boolean>
    suspend fun saveSpend(
        spendName: String,
        amount: String,
        spendTags: SpendTags,
        groupId: String,
        spentAt: Long,
        spentBy: String
    ): Flow<String>

    suspend fun getSpendAndSplitWithSpendId(spendId: String): Flow<SpendWithSplit>
    suspend fun deleteSpend(spendId: String): Flow<Boolean>
    suspend fun updateSpendSplits(
        splits: List<EditSpendTabDetails>,
        splitType: Int,
        spend: Spends
    ): Flow<Boolean>

    suspend fun updateSpend(spend: Spends): Flow<Boolean>
    fun getSpendsAfterLastSettle(groupId: String): Flow<ArrayList<SpendWithSplit>>
    suspend fun getAllUsers(): Flow<List<Users>>
    suspend fun getGroupsWithPendingBalances(
        payerId: String,
        payeeId: String
    ): Flow<List<String>>

    suspend fun saveGroupSettle(groupId: String): Flow<Boolean>
    suspend fun settleUp(
        payerId: String,
        payeeId: String,
        groupWiseAmount: HashMap<String, Double>
    ): Flow<Boolean>

    suspend fun upSync(): Flow<ApiResult<String>>
    suspend fun groupSettleCorrection(groupId: String, spendUpdatedAt: Long): Flow<Boolean>
    fun saveSpendSplits(
        split: List<EditSpendTabDetails>,
        spendId: String,
        groupId: String,
        splitType: Int
    ): Flow<Boolean>
}