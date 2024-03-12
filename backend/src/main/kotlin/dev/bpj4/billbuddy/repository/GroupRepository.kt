package dev.bpj4.billbuddy.repository

import dev.bpj4.billbuddy.entity.GroupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GroupRepository : JpaRepository<GroupEntity, String> {
    @Query(nativeQuery = true, value = "Select * from groups where id in :ids and updated_at >= :timeInSecs")
    fun findAllUpdatedRecordsById(timeInSecs: Long, ids: List<String>): List<GroupEntity>

}