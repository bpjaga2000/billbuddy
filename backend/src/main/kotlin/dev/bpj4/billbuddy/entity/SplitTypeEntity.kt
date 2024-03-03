package dev.bpj4.billbuddy.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "split_type")
data class SplitTypeEntity(
        val name: Int = 0
) : BaseLookUpEntity()