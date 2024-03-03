package dev.bpj4.billbuddy.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "roles")
data class RoleEntity(
        val role: String = ""
) : BaseLookUpEntity()