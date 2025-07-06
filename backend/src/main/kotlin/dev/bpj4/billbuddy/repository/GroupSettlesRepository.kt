package dev.bpj4.billbuddy.repository

import dev.bpj4.billbuddy.entity.GroupSettleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface GroupSettlesRepository : JpaRepository<GroupSettleEntity, String> {
    @Query(nativeQuery = true, value = "Select * from group_settles where group_id in :groupIds")
    fun findAllByGroupId(groupIds: List<String>): List<GroupSettleEntity>

    @Query(nativeQuery = true, value = "Select * from group_settles where id in :ids and updated_at >= :timeInSecs")
    fun findAllUpdatedRecordsByGroupId(timeInSecs: Long, ids: List<String>): List<GroupSettleEntity>
}