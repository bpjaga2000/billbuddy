package dev.bpj4.billbuddy.repository

import dev.bpj4.billbuddy.entity.GroupMembersEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface GroupMemberRepository : JpaRepository<GroupMembersEntity, String> {
    @Query(nativeQuery = true, value = "Select * from group_members where group_id = :groupId")
    fun findAllByGroupId(groupId: String): List<GroupMembersEntity>

    @Query(nativeQuery = true, value = "Select * from group_members where group_id in :groupIds")
    fun findAllByGroupId(groupIds: List<String>): List<GroupMembersEntity>
    fun findAllByUserId(userId: String): List<GroupMembersEntity>

    @Query(nativeQuery = true, value = "Select distinct(user_id) as user_id from group_members where group_id in (Select group_id from group_members where user_id = :userId)")
    fun fetchUserFriendIds(userId: String): List<String>

    @Query(nativeQuery = true, value = "Select * from group_members where id in :ids and updated_at < :timeInSecs")
    fun findAllUpdatedRecordsByGroupIds(timeInSecs: Long, ids: List<String>): List<GroupMembersEntity>
}