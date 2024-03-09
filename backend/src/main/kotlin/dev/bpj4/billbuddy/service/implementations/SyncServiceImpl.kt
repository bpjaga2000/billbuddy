package dev.bpj4.billbuddy.service.implementations

import dev.bpj4.billbuddy.dto.SyncDto
import dev.bpj4.billbuddy.mappings.*
import dev.bpj4.billbuddy.repository.*
import dev.bpj4.billbuddy.service.SyncService
import org.springframework.stereotype.Service

@Service
class SyncServiceImpl(
        private val userRepository: UserRepository,
        private val groupRepository: GroupRepository,
        private val groupMemberRepository: GroupMemberRepository,
        private val spendRepository: SpendRepository,
        private val spendSplitRepository: SpendSplitRepository
) : SyncService {
    override fun fetchAllData(id: String): SyncDto {
        val friendDetails = userRepository.findAllById(groupMemberRepository.fetchUserFriendIds(id)).map { it.mapToProfileDto() }
        val groups = groupRepository.findAllById(groupMemberRepository.findAllByUserId(id).map { it.groupId }).map { it.mapToGroupSyncResponseDto() }
        val groupMembers = groupMemberRepository.findAllByGroupId(groups.map { it.id }).map { it.mapToGroupMembersDto() }
        val spends = spendRepository.findAllByGroupId(groups.map { it.id }).map { it.mapToSpendDto() }
        val spendSplits = spendSplitRepository.findAllBySpendId(spends.map { it.id }).map { it.mapToSpendSplitDto() }

        return SyncDto(friendDetails, groups, groupMembers, spends, spendSplits)

    }

    override fun fetchData(id: String, timeInSecs: Long): SyncDto {

        val friendDetails = userRepository.findAllUpdatedRecordsById(timeInSecs, groupMemberRepository.fetchUserFriendIds(id)).map { it.mapToProfileDto() }
        val groups = groupRepository.findAllUpdatedRecordsById(timeInSecs, groupMemberRepository.findAllByUserId(id).map { it.groupId }).map { it.mapToGroupSyncResponseDto() }
        val groupMembers = groupMemberRepository.findAllUpdatedRecordsByGroupIds(timeInSecs, groups.map { it.id }).map { it.mapToGroupMembersDto() }
        val spends = spendRepository.findAllUpdatedRecordsByGroupIds(timeInSecs, groups.map { it.id }).map { it.mapToSpendDto() }
        val spendSplits = spendSplitRepository.findAllUpdatedRecordsBySpendId(timeInSecs, spends.map { it.id }).map { it.mapToSpendSplitDto() }

        return SyncDto(friendDetails, groups, groupMembers, spends, spendSplits)
    }

}