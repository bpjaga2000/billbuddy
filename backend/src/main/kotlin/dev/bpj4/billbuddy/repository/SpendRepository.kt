package dev.bpj4.billbuddy.repository

import dev.bpj4.billbuddy.entity.SpendEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SpendRepository : JpaRepository<SpendEntity, String> {
    @Query(nativeQuery = true, value = "Select * from spends ")
    fun findAllByGroupId(groupIds: List<String>): List<SpendEntity>

    @Query(nativeQuery = true, value = "Select * from spends where id in :ids and updated_at >= :timeInSecs")
    fun findAllUpdatedRecordsByGroupIds(timeInSecs: Long, ids: List<String>): List<SpendEntity>
}