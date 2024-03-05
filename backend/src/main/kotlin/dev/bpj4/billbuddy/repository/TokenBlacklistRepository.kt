package dev.bpj4.billbuddy.repository

import dev.bpj4.billbuddy.entity.TokenBlacklistEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenBlacklistRepository : JpaRepository<TokenBlacklistEntity, String>{
    fun existsByToken(token: String): Boolean
}