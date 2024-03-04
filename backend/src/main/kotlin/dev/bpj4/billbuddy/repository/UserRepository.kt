package dev.bpj4.billbuddy.repository

import dev.bpj4.billbuddy.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, String> {
    fun findByEmail(username: String?): Optional<UserEntity>
    fun existsByEmail(username: String?): Boolean
}