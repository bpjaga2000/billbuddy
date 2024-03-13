package dev.bpj4.billbuddy.entity


import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "token_blacklist")
data class TokenBlacklistEntity(
        @Id
        val id: String = diglol.id.Id.generate().encodeToString(),
        val token: String = "",
        val added: Long = System.currentTimeMillis() / 1000
)