package dev.bpj4.billbuddy.service.implementations

import dev.bpj4.billbuddy.dto.SyncDto
import dev.bpj4.billbuddy.dto.UpSyncDto
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
                .plus(userRepository.findById(id).get().mapToProfileDto())
        val groups = groupRepository.findAllById(groupMemberRepository.findAllByUserId(id).map { it.groupId }).map { it.mapToGroupSyncResponseDto() }
        val groupMembers = groupMemberRepository.findAllByGroupId(groups.map { it.id }).map { it.mapToGroupMembersDto() }
        val spends = spendRepository.findAllByGroupId(groups.map { it.id }).map { it.mapToSpendDto() }
        val spendSplits = spendSplitRepository.findAllBySpendId(spends.map { it.id }).map { it.mapToSpendSplitDto() }

        return SyncDto(friendDetails, groups, groupMembers, spends, spendSplits)

    }

    override fun fetchData(id: String, timeInSecs: Long): SyncDto {

        val friendDetails = userRepository.findAllUpdatedRecordsById(timeInSecs, groupMemberRepository.fetchUserFriendIds(id)).map { it.mapToProfileDto() }
                .plus(userRepository.findById(id).get().mapToProfileDto())
        val groups = groupRepository.findAllUpdatedRecordsById(timeInSecs, groupMemberRepository.findAllByUserId(id).map { it.groupId }).map { it.mapToGroupSyncResponseDto() }
        val groupMembers = groupMemberRepository.findAllUpdatedRecordsByGroupIds(timeInSecs, groups.map { it.id }).map { it.mapToGroupMembersDto() }
        val spends = spendRepository.findAllUpdatedRecordsByGroupIds(timeInSecs, groups.map { it.id }).map { it.mapToSpendDto() }
        val spendSplits = spendSplitRepository.findAllUpdatedRecordsBySpendId(timeInSecs, spends.map { it.id }).map { it.mapToSpendSplitDto() }

        return SyncDto(friendDetails, groups, groupMembers, spends, spendSplits)
    }

    override fun uploadData(id: String, upSyncDto: UpSyncDto): String {

        with(upSyncDto) {

            val spendsFromRepositoryToUpdate = spendRepository.findAllById(spends.map { it.id })
                    .filter {
                        it.updatedAtFrontend < (spends.find { user -> user.id == it.id }?.updatedAtFrontend ?: 0)
                    }
            val spendsDataToUpdate = spends.filterNot { spendsFromRepositoryToUpdate.find { repoSpends -> repoSpends.id == it.id } == null }
            spendRepository.saveAll(spendsDataToUpdate.map { it.mapToSpendEntity() })

            val spendSplitsFromRepositoryToUpdate = spendSplitRepository.findAllById(spendSplit.map { it.id })
                    .filter {
                        it.updatedAtFrontend < (spendSplit.find { user -> user.id == it.id }?.updatedAtFrontend ?: 0)
                    }
            val spendSplitsDataToUpdate = spendSplit.filterNot { spendSplitsFromRepositoryToUpdate.find { repoSpends -> repoSpends.id == it.id } == null }
            spendSplitRepository.saveAll(spendSplitsDataToUpdate.map { it.mapToSpendSplitEntity() })

        }

        return "Synced Successfully"
    }

}