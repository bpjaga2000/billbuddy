package dev.bpj4.billbuddy.entity


import diglol.id.Id
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "spends")
data class SpendEntity(

        val name: String = "",

        @Column(name = "total_amount")
        val totalAmount: Float = 0f,

        @Column(name = "is_payback")
        val isPayback: Boolean = false,

        val tag: SpendTags = SpendTags.OTHER,

        @Column(name = "group_id")
        val groupId: String,

        @Column(name = "created_by")
        var createdBy: String = "",

        @Column(name = "updated_by")
        var updatedBy: String = "",

        @Column(name = "deleted_by")
        var deletedBy: String? = null

) : BaseEntity<String>(Id.generate().encodeToString())