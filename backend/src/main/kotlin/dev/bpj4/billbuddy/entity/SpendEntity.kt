package dev.bpj4.billbuddy.entity

import com.github.shamil.Xid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "spends")
data class SpendEntity(

        @Column(name = "total_amount")
        val totalAmount: Float = 0f,

        @Column(name = "is_payback")
        val isPayback: Boolean = false,

        val tags: SpendTags = SpendTags.OTHER

) : BaseEntity<String>(Xid.string())