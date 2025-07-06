package dev.bpj4.billbuddy.entity

import io.voxkit.kotlin.nanoid.NanoId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "group_settles")
data class GroupSettleEntity(

    @Column(name = "group_id")
    val groupId: String,

    @Column(name = "settled_at")
    val settledAt: Long

) : BaseEntity<String>(NanoId.generate())