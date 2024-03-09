package dev.bpj4.billbuddy.repository

import dev.bpj4.billbuddy.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, String> {
    fun findByEmail(username: String?): Optional<UserEntity>
    fun existsByEmail(username: String?): Boolean

    @Query(nativeQuery = true, value = "Select * from users where name like '%:query%' or email like '%:query%'")// or phone like '%:query%') TODO
    fun search(@Param("query") query: String): List<UserEntity>

    @Query(nativeQuery = true, value = "Select * from users where id in :ids and updated_at > :timeInSecs")
    fun findAllUpdatedRecordsById(timeInSecs: Long, ids: List<String>): List<UserEntity>
}