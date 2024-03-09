package dev.bpj4.billbuddy.repository

import dev.bpj4.billbuddy.entity.SpendSplitEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SpendSplitRepository : JpaRepository<SpendSplitEntity, String> {
    @Query(nativeQuery = true, value = "Select * from spend_splits where spend_id = :spendId")
    fun findAllBySpendId(spendId: List<String>): List<SpendSplitEntity>

    @Query(nativeQuery = true, value = "Select * from spends where id in :ids and updated_at < :timeInSecs")
    fun findAllUpdatedRecordsBySpendId(timeInSecs: Long, ids: List<String>): List<SpendSplitEntity>
}