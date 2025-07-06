package data.model.dto

import data.model.dto.GroupMembersDto
import data.model.dto.GroupSyncResponseDto
import data.model.dto.ProfileDto
import data.model.dto.SpendDto
import data.model.dto.SpendSplitDto
import kotlinx.serialization.Serializable

@Serializable
data class SyncDto(
    val users: List<ProfileDto>,
    val groups: List<GroupSyncResponseDto>,
    val groupMembers: List<GroupMembersDto>,
    val spends: List<SpendDto>,
    val spendSplit: List<SpendSplitDto>,
    val groupSettles: List<GroupSettlesDto>
)