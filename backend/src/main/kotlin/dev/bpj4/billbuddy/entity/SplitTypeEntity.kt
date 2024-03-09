package dev.bpj4.billbuddy.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "split_type")
data class SplitTypeEntity(
        val method: Int = 0
) : BaseLookUpEntity()